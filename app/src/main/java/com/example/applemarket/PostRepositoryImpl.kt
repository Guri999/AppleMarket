package com.example.applemarket

interface PostRepository{
    fun setList(): MutableList<Post>
    /**
     * TODO 데이터에서 특정 포스트 제거
     *
     * @param position 포스트 위치
     */
    fun deletePost(position: Int)
    /**
     * TODO 좋아요 버튼 클릭
     *
     * @param data totalPost에서 출력되는 포스트
     * totalPost에서 data와 같은 요소를 찾고 값을 변경해줌
     * 라이브 데이터로 옵저빙중이라 값이 바뀌면 액티비티에서 자동으로 갱신된다
     * 본래라면 UseCase에서 로직을 수행하는게 맞겠지만
     * 그렇게되면 보일러 플레이트 코드도 많아지고 간단한 로직이기 때문에 그냥 Repository에서 수행
     */
    fun onClickLike(data: Post): Post?
}
class PostRepositoryImpl: PostRepository {

    var totalPost: MutableList<Post> = mutableListOf()
    var detailPost: Post? = null

    override fun setList(): MutableList<Post> {
        totalPost = PostData.totalPost
        return totalPost
    }

    override fun deletePost(position: Int) {
        PostData.totalPost.removeAt(position)
        totalPost = PostData.totalPost
    }

    override fun onClickLike(data: Post): Post? {
        PostData.totalPost.find { it == data }?.let { post ->
            if (data.userlike) {
                post.userlike = false
                post.like--
            } else {
                post.userlike = true
                post.like++
            }
            detailPost = post
        }
        totalPost = PostData.totalPost
        return detailPost
    }

}

