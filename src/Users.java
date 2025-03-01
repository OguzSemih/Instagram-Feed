import java.util.ArrayList;

public class Users {
    private String id;
    private MyHashTable<String,Users> following;
    private MyHashTable<String,Posts> seenPosts;
    private ArrayList<Posts> postsArray;
    private MyHashTable<String,Posts> likedPosts;



    public Users(String id) {
        this.id = id;
        following = new MyHashTable<>();
        seenPosts = new MyHashTable<>();
        postsArray = new ArrayList<>();
        likedPosts = new MyHashTable<>();
    }
    public MyHashTable<String,Posts> getLikedPosts() {
        return likedPosts;
    }
    public ArrayList<Posts> getPostsArray() {
        return postsArray;
    }
    public MyHashTable<String,Posts> getSeenPosts() {
        return seenPosts;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public MyHashTable<String,Users> getFollowing() {
        return following;
    }

    public void setFollowing(MyHashTable<String,Users> following) {
        this.following = following;
    }

}
