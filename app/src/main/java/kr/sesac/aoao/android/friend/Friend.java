package kr.sesac.aoao.android.friend;

/**
 * @since 2024.01.28
 * @author 최정윤
 */
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

