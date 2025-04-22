public interface BankAccountService {
    void createBankAccount(BankAccount bankAccount);
    Optional<BankAccount> getBankAccount(long id);
    List<BankAccount> getBankAccountsOfUser(User user, int limit);
    void updateBankAccount(BankAccount bankAccount);
    void deleteBankAccount(BankAccount bankAccount);
}