package com.kiwi.clientside;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.kiwi.model.Action;
import com.kiwi.model.Preset;
import com.kiwi.model.Response;
import com.kiwi.model.User;


public class ClientController {
    private User user;
    private String baseUrl = "https://oppy-project.herokuapp.com/";
    private RestTemplate restTemplate = new RestTemplate();
    private List<Action> actionList = null;
    private ResponseEntity<String> responseEntity = null;
    private List<User> top50 = null;
    private ObjectMapper objectMapper = new ObjectMapper();


    public ClientController(User user, Boolean hashed){
        this.user = user;
        if(!hashed){
            user.setPassword(hash(user.getPassword()));
        }
    }


    public ClientController() {
    }

    private enum Path {
        REGISTER {
            public String toString() {
                return "register";
            }
        }, AVAILABILITY {
            public String toString() {
                return "nameavailable?username=%s";
            }
        }, LOGIN {
            public String toString() {
                return "login";
            }
        }, SCORE {
            public String toString() {
                return "score?username=%s";
            }
        }, ACTIONS {
            public String toString() {
                return "actions";
            }
        }, TAKEACTION {
            public String toString() {
                return "takeaction?username=%s";
            }
        }, DELETE {
            public String toString() {
                return "delete";
            }
        }, UPDATEPASS {
            public String toString() {
                return "updatepass?newpass=%s";
            }
        }, EMAIL {
            public String toString() {
                return "email?username=%s";
            }
        }, TAKEACTIONS {
            public String toString() {
                return "takeactions?username=%s";
            }
        }, UPDATEEMAIL {
            public String toString() {
                return "updateEmail?newEmail=%s";
            }
        }, TOP50 {
            public String toString() {
                return "top50";
            }
        }, CHANGEANON {
            public String toString() {
                return "changeAnonymous?anonymous=%s";
            }
        }, RESET {
            public String toString() {
                return "reset";
            }
        }, ADDFRIEND {
            public String toString() {
                return "addfriend?username=%s";
            }
        }, GETFRIENDS {
            public String toString() {
                return "friends?username=%s";
            }
        }, DELETEFRIEND {
            public String toString() {
                return "deletefriend?username=%s";
            }
        }, ADDPRESET {
            public String toString() {
                return "addpreset?username=%s";
            }
        }, GETPRESETS {
            public String toString() {
                return "presets?username=%s";
            }
        }, DELETEPRESET {
            public String toString() {
                return "deletepreset?username=%s";
            }
        }, USERINFO {
            public String toString() {
                return "userinfo";
            }
        }, SETPROFILEPIC {
            public String toString() {
                return "setprofilepic";
            }
        }, GETPROFILEPIC {
            public String toString() {
                return "getprofilepic?username=%s";
            }
        }, POSITION {
            public String toString() {
                return "position?username=%s";
            }
        }

    }

    /**
     * Fetches the profile picture base 64 string from the server.
     * @param username who the profile picture belongs to.
     * @return Image (profile picture).
     */
//    public BufferedImage getProfilePic(String username) {
//        responseEntity = this.getRequest(this.baseUrl + String.format(Path.GETPROFILEPIC.toString(), username));
//        return ImageHandler.decodeToImg(new JSONObject(responseEntity.getBody()).getString("message"));
//    }

    /**
     * Sends the base64 encoded profile picture string to the server.
     * @param encodedStr the img file to be encoded and sent.
     * @return String response message ("true"/"false").
     */
    public String updateProfilePic(String encodedStr) {
        this.user.setProfilePicture(encodedStr);
        responseEntity = this.postRequest(this.baseUrl + Path.SETPROFILEPIC.toString(), user);
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            return "false";
        }
    }

    /**
     * Deletes the given preset from the user's presetList on the server.
     *
     * @param preset the preset to delete (only preset name is required, the rest can be null)
     * @return String response message ("true"/"false").
     */
    public String deletePreset(Preset preset) {
        responseEntity = this.postRequest(this.baseUrl
                + String.format(Path.DELETEPRESET.toString(), this.user.getUsername()), preset);

        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Updates this.user's presetsList by downloading a com.kiwi.oppy.User (preset) list from server and setting
     * the user's presets to the mentioned list.
     */
    public void updateUserPresets() {
        responseEntity = this.getRequest(this.baseUrl
                + String.format(Path.GETPRESETS.toString(), this.user.getUsername()));
        try {
            this.user.setPresets(objectMapper.<List<Preset>>readValue(responseEntity.getBody(), new TypeReference<List<Preset>>() {
            }));
        } catch (IOException e) {
            System.out.println("ErrorUpdatePresets");
        }
    }

    /**
     * Sends a post request to the server requesting to add a preset to a user's preset list.
     *
     * @param preset the preset (type: Preset) to be added.
     * @return String response message ("true"/"false").
     */
    public String addPreset(Preset preset) {
        responseEntity = this.postRequest(this.baseUrl
                + String.format(Path.ADDPRESET.toString(), this.user.getUsername()), preset);
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "false";
    }

    /**
     * Updates this.user's friendlist by downloading a com.kiwi.oppy.User (friend) list from server and setting
     * the user's friends to the mentioned list.
     */
    public void updateFriendList() {
        responseEntity = this.getRequest(this.baseUrl
                + String.format(Path.GETFRIENDS.toString(), this.user.getUsername()));
        try {
            this.user.setFriends(objectMapper.<List<User>>readValue(responseEntity.getBody(), new TypeReference<List<User>>() {}));
        } catch (IOException e) {
            System.out.println("ErrorUpdateFriendList");
        }
    }


    /**
     * Sends a get request to server to add a friend to the this.user's friend list.
     *
     * @param friend the the friend (com.kiwi.oppy.User type) to be added
     * @return String response message ("true"/"false").
     */
    public String addFriend(User friend) {
        responseEntity = this.postRequest(this.baseUrl
                + String.format(Path.ADDFRIEND.toString(), this.user.getUsername()), friend);
        try {
            return (objectMapper.readValue(responseEntity.getBody(), Response.class)).getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sends a post request to server to delete a friend from the user's friend list.
     *
     * @param friendToDelete (com.kiwi.oppy.User) friend to delete.
     * @return String response message ("true"/"false").
     */
    public String deleteFriend(User friendToDelete) {
        responseEntity = this.postRequest(this.baseUrl
                + String.format(Path.DELETEFRIEND.toString(), this.user.getUsername()), friendToDelete);
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sends a new user registration request to server.
     *
     * @return String response message ("true"/"false").
     */
    public String register() {
        responseEntity = this.postRequest(this.baseUrl + Path.REGISTER.toString(), user);
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sends an "update user pass" request to the server.
     *
     * @param newPass the new password for the user.
     * @param oldPlainPass old password for verification.
     * @return String response message ("true"/"false").
     */
    public String updatePass(String newPass, String oldPlainPass) {
        if (hash(oldPlainPass).equals(this.user.getPassword())) {
            responseEntity = this.postRequest(this.baseUrl
                    + String.format(Path.UPDATEPASS.toString(), hash(newPass)), user);
            try {
                return new JSONObject(responseEntity.getBody()).getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "false";
    }

        /**
         * Sends a "take action request" to the server.
         *
         * @param actionName action name to be sent.
         * @return String response msg ("true"/"false").
         */
    public String takeAction(String actionName) {
        responseEntity = this.postRequest(this.baseUrl + String.format(Path.TAKEACTION.toString(),
                user.getUsername()), new Action(actionName, "", 0));
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sends a "get email request" to the server.
     *
     * @return String response msg containing user's email addy.
     */
    public String getEmail() {
        responseEntity = this.getRequest(this.baseUrl + String.format(Path.EMAIL.toString(), user.getUsername()));
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns the list of actions.
     *
     * @return actionList.
     */
    public List<Action> getActionList() {
        return this.actionList;
    }

    /**
     * Updates the list of actions by downloading the action list from the server.
     */
    public void updateActionList() {
        responseEntity = this.getRequest(this.baseUrl + String.format(Path.ACTIONS.toString()));
        try {
            actionList = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<Action>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a "delete acct request" to the server.
     *
     * @return String response msg ("true"/"false"), implying success or failure.
     */
    public String deleteAccount() {
        responseEntity = this.postRequest(this.baseUrl + Path.DELETE.toString(), user);
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sends an "update email addy request" to the server.
     *
     * @param newEmail the new email addy.
     * @param pass     current password.
     * @return String response msg ("true"/"false").
     */
    public String updateEmail(String newEmail, String pass) {
        if (hash(pass).equals(this.user.getPassword())) {
            responseEntity = this.postRequest(this.baseUrl
                    + String.format(Path.UPDATEEMAIL.toString(), newEmail), user);
            String responseMsg = null;
            try {
                responseMsg = new JSONObject(responseEntity.getBody()).getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseMsg;
        } else {
            return "false";
        }
    }

    /**
     * Sends a "reset request" to the server.
     *
     * @return String resposne msg ("true"/"false").
     */
    public String reset() {
        responseEntity = this.postRequest(this.baseUrl + Path.RESET, this.user);
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sends a "login request" to the server.
     *
     * @return String response msg "true"/"false"
     */
    public String login() {
        responseEntity = this.postRequest(this.baseUrl + Path.LOGIN.toString(), user);
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sends a request to server to get user's score.
     *
     * @return user's score contained in content body.
     */
    public String getScore() {
        responseEntity = this.getRequest(this.baseUrl + String.format(Path.SCORE.toString(), user.getUsername()));
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Sends a request to the server checking the availability of a username.
     *
     * @param username username to be checked.
     * @return availability "true"/"false" contained in response msg.
     */
    public String checkAvailability(String username) {
        responseEntity = this.getRequest(this.baseUrl + String.format(Path.AVAILABILITY.toString(), username));
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * Post request method.
     *
     * @param url url to point at.
     * @param obj object to send.
     * @return ResponseEntity containing status code/body.
     */
    private ResponseEntity<String> postRequest(String url, Object obj) {
        return restTemplate.postForEntity(url, obj, String.class);
    }

    /**
     * Get request method.
     *
     * @param url to point at.
     * @return ResponseEntity containing status code/body.
     */
    private ResponseEntity<String> getRequest(String url) {
        return restTemplate.getForEntity(url, String.class);
    }

    /**
     * Method to subdivide list of actions according to given cat name.
     *
     * @param categoryName cat name to subdivide.
     * @return return list of actions from only that category.
     */
    public List<Action> getCategoryList(String categoryName) {
        List<Action> categoryList = new ArrayList<>();
        if (this.actionList != null) {
            for (Action act : actionList) {
                if (act.getCategory().equals(categoryName)) {
                    categoryList.add(act);
                }
            }
            return categoryList;
        }
        return null;
    }

    public User getUser() {
        return this.user;
    }

    public String hash(String pwd) {
        return Hashing.sha256().hashString(pwd, StandardCharsets.UTF_8).toString();
    }

    /**
     * Method to update the top50 arraylist with the users who have the most points.
     */
    public void updateTop50() {
        responseEntity = this.getRequest(this.baseUrl + String.format(Path.TOP50.toString()));
        try {
            top50 = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getTop50() {
        return top50;
    }

    /**
     * Updates the this.user.
     */
    public void updateUser() {
        responseEntity = this.postRequest(this.baseUrl
                + Path.USERINFO.toString(), user);
        try {
            this.user = objectMapper.readValue(responseEntity.getBody(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    /**
     * Sends an "update pushNotifications request" to the server.
     *
     * @param trueOrFalse tells if anonymous should be set to true or false.
     */
    public String updateAnonymous(boolean trueOrFalse) {
        responseEntity = this.postRequest(this.baseUrl
                + String.format(Path.CHANGEANON.toString(), trueOrFalse), user);
        String responseMsg = null;
        try {
            responseMsg = new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.user.setAnonymous(trueOrFalse);
        return responseMsg;
    }

    /**
     * changes the email value of a list of users in decending order.
     */
    public void top50Ranks(List<User> list) {
        for (int i = 1; i <= list.size(); i++) {
            list.get(i - 1).setEmail("" + i);
        }
    }

    public String getPosition() {
        responseEntity = this.getRequest(this.baseUrl + String.format(Path.POSITION.toString(), user.getUsername()));
        try {
            return new JSONObject(responseEntity.getBody()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


}
