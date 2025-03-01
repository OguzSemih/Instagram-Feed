import java.util.ArrayList;

public class Management {
    MyHashTable<String, Users> usersTable = new MyHashTable();
    MyHashTable<String, Posts> allPostsInHash = new MyHashTable();
    boolean indicator1; // this is to understand whether more post is available
    boolean indicator2; // this is to check that there is such user in generate feed method
    boolean indicator3; // this is to check that there is such user in scroll method
    boolean indicator4; // this is to understand whether more post is available in scroll method
    boolean indicator5;


    public String createUser(String userId) {
        if (usersTable.containsKey(userId)) {
            return "Some error occurred in create_user.";
        } else {
            Users user = new Users(userId);
            usersTable.put(userId, user);
            return "Created user with Id " + userId + ".";
        }

    }

    public String followUser(String userId1, String userId2) {
        if (usersTable.containsKey(userId1) && usersTable.containsKey(userId2)) {
            Users user1 = usersTable.get(userId1);
            Users user2 = usersTable.get(userId2);
            if (user1.getFollowing().containsKey(user2.getId()) || user1 == user2) {
                return "Some error occurred in follow_user.";
            } else {
                user1.getFollowing().put(user2.getId(), user2);
                return userId1 + " followed " + userId2 + ".";
            }
        } else {
            return "Some error occurred in follow_user.";
        }

    }

    public String unfollowUser(String userId1, String userId2) {
        if (usersTable.containsKey(userId1) && usersTable.containsKey(userId2)) {
            Users user1 = usersTable.get(userId1);
            Users user2 = usersTable.get(userId2);
            if (user1.getFollowing().containsKey(user2.getId())) {
                user1.getFollowing().remove(user2.getId());
                return userId1 + " unfollowed " + userId2 + ".";
            } else {
                return "Some error occurred in unfollow_user.";
            }
        } else {
            return "Some error occurred in unfollow_user.";
        }
    }

    public String createPost(String userId, String postId, String content) {
        if (!usersTable.containsKey(userId) || allPostsInHash.containsKey(postId)) {
            return "Some error occurred in create_post.";
        } else {
            Users user = usersTable.get(userId);
            Posts post = new Posts(postId, content, user);
            user.getPostsArray().add(post);
            allPostsInHash.put(postId, post);
            return userId + " created a post with Id " + postId + ".";
        }

    }

    public String seePost(String userId, String postId) {
        if (!usersTable.containsKey(userId) || !allPostsInHash.containsKey(postId)) {
            return "Some error occurred in see_post.";
        } else {
            Users user = usersTable.get(userId);
            Posts post = allPostsInHash.get(postId);
            user.getSeenPosts().put(post.getId(), post);
            post.getSeenUsers().put(user.getId(), user);
            return userId + " saw " + postId + ".";
        }
    }

    public String seeAllPostsFromUser(String userId1, String userId2) {
        if (!usersTable.containsKey(userId1) || !usersTable.containsKey(userId2)) {
            return "Some error occurred in see_all_posts_from_user.";
        } else {
            Users user1 = usersTable.get(userId1);
            Users user2 = usersTable.get(userId2);
            for (Posts post : user2.getPostsArray()) {
                user1.getSeenPosts().put(post.getId(), post);
                post.getSeenUsers().put(user1.getId(), user1);
            }
            return userId1 + " saw all posts of " + userId2 + ".";
        }
    }

    public String toggleLike(String userId, String postId) {
        if (!usersTable.containsKey(userId) || !allPostsInHash.containsKey(postId)) {
            return "Some error occurred in toggle_like.";
        }
        Users user = usersTable.get(userId);
        Posts post = allPostsInHash.get(postId);
        if (user.getLikedPosts().containsKey(postId)) {
            user.getLikedPosts().remove(postId);
            post.getLikedUsers().remove(userId);
            post.setLikeNumber(post.getLikeNumber() - 1);
            return userId + " unliked " + postId + ".";
        }

        user.getLikedPosts().put(post.getId(), post); // add post to liked posts
        post.setLikeNumber(post.getLikeNumber() + 1); // increase like number by 1
        post.getLikedUsers().put(userId, user); // add user to liked users
        if (!user.getSeenPosts().containsKey(postId)) { // if post not seen, see post
            seePost(userId, postId);
        }
        return userId + " liked " + postId + ".";

    }

    public ArrayList<Posts> generateFeed(String userId, int num) {
        indicator1 = false; // if it is false there is more posts available
        indicator2 = false;// if it is false there is such user, if not there is no such user
        ArrayList<Posts> temporaryPostHolder = new ArrayList<>();// arraylist that holds posts temporary
        ArrayList<Posts> postHolder = new ArrayList<>();// arraylist that holds posts to be able to log it
        if (!usersTable.containsKey(userId)) {
            indicator2 = true; // there is no such user
            return postHolder;
        }
        MyMaxHeap<Posts> feedPostHeap = new MyMaxHeap<>();
        Users user = usersTable.get(userId); // feed will be generated for this user
        for (Users followingUser : user.getFollowing()) {
            for (Posts post : followingUser.getPostsArray()) {
                if (!user.getSeenPosts().containsKey(post.getId())) {
                    temporaryPostHolder.add(post); // all posts that is not seen is added till posts number reaches num

                }
            }

        }



        for (Posts post : temporaryPostHolder) {
            feedPostHeap.insert(post);
        }

        int loopCount = feedPostHeap.getSize();
        if(num >= loopCount) {
            for (int i = 0; i < loopCount; i++) {
                postHolder.add(feedPostHeap.extractMax());
            }
        }
        else{
            for (int i = 0; i < num; i++) {
                postHolder.add(feedPostHeap.extractMax());
            }
        }

        // this is to determine whether "no more posts available" is written or not
        if(postHolder.size() < num) {
            indicator1 = true;
        }


        return postHolder;
    }


    public ArrayList<Posts> scrollThoroughFeed(String userId,int num, Integer... nums) {
        indicator3 = false; // first false, if user with userId does not exist, it will be true
        indicator4 = false; // if it is false, always there are posts
        ArrayList<Posts> scrollHolder = new ArrayList<>(); // this holds posts that are seen or liked while scrolling
        if(!usersTable.containsKey(userId)) { // it checks whether user exists
            indicator3 = true;// user does not exist
            return scrollHolder;
        }
        Users user = usersTable.get(userId);
        ArrayList<Posts> myPostHolder = generateFeed(userId,num);

        if(num > myPostHolder.size()) {
            indicator4 = true; // there is not more posts
        }

        for(int i = 0; i< myPostHolder.size(); i++) {
            if(i >= num) {
                break;
            }
            if (nums[i] == 1) {
                toggleLike(userId,myPostHolder.get(i).getId());// press like button
                scrollHolder.add(myPostHolder.get(i));// the post that are added to scrollHolder is liked

            }
            else if (nums[i] == 0) {
                seePost(userId, myPostHolder.get(i).getId());
                scrollHolder.add(myPostHolder.get(i)); // the post that are added to scrollHolder is seen
            }
        }
        return scrollHolder;


    }



    public ArrayList<Posts> sortPosts(String userId) {
        indicator5 = false; // if it is false,  always there are more posts
        ArrayList<Posts> postHolder = new ArrayList<>();// arraylist that holds posts to be able to log it
        if (!usersTable.containsKey(userId)) {
            indicator5 = true; // there is no such user
            return postHolder;
        }
        Users user = usersTable.get(userId); // sorting will be generated for this user
        MyMaxHeap<Posts> postHeapOfUser = new MyMaxHeap<>();
        for (Posts post: user.getPostsArray()) { // this is to create post heap of user
            postHeapOfUser.insert(post);
        }
        for (int i = 0;i < user.getPostsArray().size();i++) { // means that create posts in feed num times
            postHolder.add(postHeapOfUser.extractMax()); //  get extract it to reach another most liked post
        }

        return postHolder;
    }

}
