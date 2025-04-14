public interface BankAccountService
{
    void createBankAccount(BankAccount bankAccount);

    Optional<BankAccount> getBankAccount(long id);

    Optional<BankAccount> getBankAccountOfUserByName(User user, String name);

    List<BankAccount> getBankAccountsOfUser(User user, int limit);

    void updateBankAccount(BankAccount bankAccount);

    void deleteBankAccount(BankAccount bankAccount);
}