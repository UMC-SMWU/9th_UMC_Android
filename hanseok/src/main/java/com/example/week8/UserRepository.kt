import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.example.week8.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun signUp(user: User, onResult: (isSuccess: Boolean, message: String) -> Unit) {
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        user.Id = firebaseUser.uid

                        database.child(firebaseUser.uid).setValue(user.copy(password = ""))
                            .addOnSuccessListener {
                                onResult(true, "회원가입이 완료되었습니다.")
                            }
                            .addOnFailureListener {
                                onResult(false, it.message ?: "데이터베이스 저장에 실패했습니다.")
                            }
                    } else {
                        onResult(false, "사용자 정보를 가져올 수 없습니다.")
                    }
                } else {

                    val message = task.exception?.message ?: "알 수 없는 오류가 발생했습니다."
                    onResult(false, message)
                }
            }
    }
    fun login(email: String, password: String, onResult: (isSuccess: Boolean, message: String, user: User?) -> Unit) {
        // 1. FirebaseAuth를 사용하여 이메일과 비밀번호로 로그인 시도
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 2. 로그인 성공 시, 현재 사용자의 UID를 가져옴
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        // 3. UID를 사용하여 데이터베이스에서 해당 사용자 정보 검색
                        database.child(firebaseUser.uid).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                // 4. 데이터베이스에서 가져온 정보를 User 객체로 변환
                                val userFromDb = snapshot.getValue(User::class.java)

                                if (userFromDb != null) {
                                    // 5. 성공 콜백 호출 (성공, 메시지, User 객체 반환)
                                    onResult(true, "로그인에 성공했습니다.", userFromDb)
                                } else {
                                    // 데이터는 없지만 인증은 된 비정상적인 경우
                                    onResult(false, "사용자 정보를 데이터베이스에서 찾을 수 없습니다.", null)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // 데이터베이스 검색 실패
                                onResult(false, error.message, null)
                            }
                        })
                    } else {
                        // 인증은 성공했지만 currentUser가 null인 경우
                        onResult(false, "사용자 정보를 가져오는데 실패했습니다.", null)
                    }
                } else {
                    // 6. 로그인 실패 시 (비밀번호 틀림, 없는 계정 등)
                    val message = task.exception?.message ?: "이메일 또는 비밀번호가 올바르지 않습니다."
                    onResult(false, message, null)
                }
            }
    }
}