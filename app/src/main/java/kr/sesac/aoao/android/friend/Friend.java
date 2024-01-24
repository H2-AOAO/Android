package kr.sesac.aoao.android.friend;

public class Friend {
    private String name;
    private int profileImageResource;

    public Friend(String name, int profileImageResource) {
        this.name = name;
        this.profileImageResource = profileImageResource;
    }

    public String getName() {
        return name;
    }

    public int getProfileImageResource() {
        return profileImageResource;
    }
}

