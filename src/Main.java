import java.io.*;
import java.util.ArrayList;

public class Main {
    static Management ourFeed = new Management();
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
             BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))) {
             String line;
             while((line = reader.readLine()) != null) {
                 String output = processCommand(line);

                    // write the output to output.txt
                 if (output.length() != 0) {
                     writer.write(output);
                     writer.newLine();
                 }

             }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processCommand(String command) {

        // seperating input
        String[] parts = command.split(" ");

        if (parts[0].equals("create_user")) {
            return ourFeed.createUser(parts[1]);
        }
        if (parts[0].equals("follow_user")) {
            return ourFeed.followUser(parts[1],parts[2]);
        }
        if (parts[0].equals("unfollow_user")) {
            return ourFeed.unfollowUser(parts[1],parts[2]);
        }
        if (parts[0].equals("create_post")) {
            return ourFeed.createPost(parts[1], parts[2],parts[3]);
        }
        if (parts[0].equals("see_post")) {
            return ourFeed.seePost(parts[1], parts[2]);
        }
        if (parts[0].equals("see_all_posts_from_user")) {
            return ourFeed.seeAllPostsFromUser(parts[1], parts[2]);
        }
        if (parts[0].equals("toggle_like")) {
            return ourFeed.toggleLike(parts[1], parts[2]);
        }
        if (parts[0].equals("generate_feed")) {
            int num = Integer.parseInt(parts[2]);
            ArrayList<Posts> returnedArrayListFromManager = ourFeed.generateFeed(parts[1],num);
            if (returnedArrayListFromManager.isEmpty() && ourFeed.indicator2) {
                return "Some error occurred in generate_feed.";
            }

            StringBuilder result1 = new StringBuilder(); // to write to console
            result1.append("Feed for ");
            result1.append(parts[1]);
            result1.append(":");
            result1.append("\n");
            for(Posts post: returnedArrayListFromManager) {
                result1.append("Post ID: ");
                result1.append(post.getId());
                result1.append(", ");
                result1.append("Author: ");
                result1.append(post.getAuthor().getId());
                result1.append(", ");
                result1.append("Likes: ");
                result1.append(post.getLikeNumber());
                result1.append("\n"); // to go to down line
            }
            if (!ourFeed.indicator1) {// there are more posts
                result1.deleteCharAt(result1.length() -1);
                return result1.toString();
            }
            else {
                result1.append("No more posts available for ");
                result1.append(parts[1]);
                result1.append(".");
                return result1.toString();
            }

        }


        if (parts[0].equals("scroll_through_feed")) {
            int num = Integer.parseInt(parts[2]);
            Integer[] dynamicNums = new Integer[num];
            for(int i = 0;i < num; i++) {
                int nums = Integer.parseInt(parts[i+3]);
                dynamicNums[i] = nums;
            }
            ArrayList<Posts> postsArrayList = ourFeed.scrollThoroughFeed(parts[1],num,dynamicNums);
            if (postsArrayList.isEmpty() && ourFeed.indicator3) { // there is no such user
                return "Some error occurred in scroll_through_feed.";
            }
            Users currentUser = ourFeed.usersTable.get(parts[1]);
            StringBuilder result2 = new StringBuilder(); // this is to write to txt
            result2.append(parts[1]);
            result2.append(" is scrolling through feed:");
            result2.append("\n");
            for(Posts post: postsArrayList) {
                if(post.getLikedUsers().containsKey(currentUser.getId())) {
                    result2.append(parts[1]);
                    result2.append(" saw ");
                    result2.append(post.getId());
                    result2.append(" while scrolling and clicked the like button.");
                    result2.append("\n"); // to go to down line
                }
                else {
                    result2.append(parts[1]);
                    result2.append(" saw ");
                    result2.append(post.getId());
                    result2.append(" while scrolling.");
                    result2.append("\n"); // to go to down line
                }
            }
            if (!ourFeed.indicator4) {
                result2.deleteCharAt(result2.length() -1);
                return result2.toString();
            }
            else {
                result2.append("No more posts in feed.");
                return result2.toString();
            }
        }

        if (parts[0].equals("sort_posts")) {
            ArrayList<Posts> returnedArrayListFromManager = ourFeed.sortPosts(parts[1]);
            if (returnedArrayListFromManager.isEmpty() && ourFeed.indicator5) {
                return "Some error occurred in sort_posts.";
            }
            if (returnedArrayListFromManager.isEmpty()) {
                return "No posts from " + parts[1] + ".";
            }

            StringBuilder result3 = new StringBuilder(); // this is to write to txt
            result3.append("Sorting ");
            result3.append(parts[1]);
            result3.append("'s posts:");
            result3.append("\n");
            for(Posts post: returnedArrayListFromManager) {
                result3.append(post.getId());
                result3.append(", Likes: ");
                result3.append(post.getLikeNumber());
                result3.append("\n"); // to go to down line
            }
            result3.deleteCharAt(result3.length() -1);
            return result3.toString();



        }
        return "";
    }


}
