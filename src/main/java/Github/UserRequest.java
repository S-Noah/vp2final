/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Github;

import java.io.Serializable;

/**
 *
 * @author Noahb
 */
public class UserRequest implements Serializable{
    // Identification Fields.
    protected String login;  
    protected int id;
    protected String node_id;
    // API Extenstion Fields.
    protected String avatar_url;  
    protected String url;
    protected String html_url;
    protected String followers_url;
    protected String following_url;
    protected String gists_url;
    protected String starred_url;
    protected String subscriptions_url;
    protected String organizations_url;
    protected String repos_url;
    protected String events_url;
    protected String received_events_url;
    // UserRequest Statistic Fields.
    protected String type;
    protected boolean site_admin;
    protected String name;
    protected String bio;
    protected int public_repos;
    protected int followers;
    protected int following;
    protected String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getNode_id() {
        return node_id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public String getUrl() {
        return url;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getFollowers_url() {
        return followers_url;
    }

    public String getFollowing_url() {
        return following_url;
    }

    public String getGists_url() {
        return gists_url;
    }

    public String getStarred_url() {
        return starred_url;
    }

    public String getSubscriptions_url() {
        return subscriptions_url;
    }

    public String getOrganizations_url() {
        return organizations_url;
    }

    public String getRepos_url() {
        return repos_url;
    }

    public String getEvents_url() {
        return events_url;
    }

    public String getReceived_events_url() {
        return received_events_url;
    }

    public String getType() {
        return type;
    }

    public boolean isSite_admin() {
        return site_admin;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }
    
    @Override
    public String toString(){
        return "login: " + login + ", ID: " + id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public void setFollowers_url(String followers_url) {
        this.followers_url = followers_url;
    }

    public void setFollowing_url(String following_url) {
        this.following_url = following_url;
    }

    public void setGists_url(String gists_url) {
        this.gists_url = gists_url;
    }

    public void setStarred_url(String starred_url) {
        this.starred_url = starred_url;
    }

    public void setSubscriptions_url(String subscriptions_url) {
        this.subscriptions_url = subscriptions_url;
    }

    public void setOrganizations_url(String organizations_url) {
        this.organizations_url = organizations_url;
    }

    public void setRepos_url(String repos_url) {
        this.repos_url = repos_url;
    }

    public void setEvents_url(String events_url) {
        this.events_url = events_url;
    }

    public void setReceived_events_url(String received_events_url) {
        this.received_events_url = received_events_url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSite_admin(boolean site_admin) {
        this.site_admin = site_admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
    
    protected void copy(UserRequest userRequest){
        login = userRequest.getLogin();  
        id = userRequest.getId();
        node_id = userRequest.getNode_id();
        avatar_url = userRequest.getAvatar_url();
        url = userRequest.getUrl();
        html_url = userRequest.getHtml_url();
        followers_url = userRequest.getFollowers_url();
        following_url = userRequest.getFollowing_url();
        gists_url = userRequest.getGists_url();
        starred_url = userRequest.getStarred_url();
        subscriptions_url = userRequest.getSubscriptions_url();
        organizations_url = userRequest.getOrganizations_url(); 
        repos_url = userRequest.getRepos_url();
        events_url = userRequest.getEvents_url();
        received_events_url = userRequest.getReceived_events_url();

        type = userRequest.getType();
        site_admin = userRequest.isSite_admin();
        name = userRequest.getName();
        bio = userRequest.getBio();
        public_repos = userRequest.getPublic_repos();
        followers = userRequest.getFollowers();
        following = userRequest.getFollowing();
        created_at = userRequest.getCreated_at();
    }
}
