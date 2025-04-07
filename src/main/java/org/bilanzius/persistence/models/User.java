package org.bilanzius.persistence.models;

import org.bilanzius.utils.HashedPassword;

public class User
{

    public static User createUser(String username, HashedPassword password)
    {
        return new User(0, username, password, 0);
    }

    private final int id;
    private final String username;
    private HashedPassword hashedPassword;
    private int mainBankAccountId;

    public User(int id, String username, HashedPassword hashedPassword, int mainBankAccountId)
    {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.mainBankAccountId = mainBankAccountId;
    }

    public boolean canBeUpdated()
    {
        return id > 0;
    }

    public int getMainBankAccountId()
    {
        return this.mainBankAccountId;
    }

    public void setMainAccountId(int mainAccountId)
    {
        this.mainBankAccountId = mainAccountId;
    }

    public int getId()
    {
        return id;
    }

    public HashedPassword getHashedPassword()
    {
        return hashedPassword;
    }

    public void setHashedPassword(HashedPassword hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public String getUsername()
    {
        return username;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", username" +
                "='" + username + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", mainAccountId='" + mainBankAccountId + '\'' +
                '}';
    }
}
