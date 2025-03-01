public class Posts implements Comparable<Posts>{
    private String id;
    private Users author;
    private String content;
    private int likeNumber;
    private MyHashTable<String, Users> likedUsers;
    private MyHashTable<String,Users> seenUsers;

    public String getId() {
        return id;
    }


    public String getContent() {
        return content;
    }


    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public MyHashTable<String, Users> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(MyHashTable<String, Users> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public MyHashTable<String, Users> getSeenUsers() {
        return seenUsers;
    }

    public Users getAuthor() {
        return author;
    }

    public Posts(String id, String content, Users author) {// constructor
        this.author = author;
        this.id = id;
        this.content = content;
        likeNumber = 0;
        likedUsers = new MyHashTable<>();
        seenUsers = new MyHashTable<>();
    }
    public int compareTo(Posts other) {
        if (this.likeNumber != other.likeNumber) {
            return Integer.compare(this.likeNumber, other.likeNumber);
        }
        return this.id.compareTo(other.id); // this gives priority to lexicographically bigger one
    }
}
