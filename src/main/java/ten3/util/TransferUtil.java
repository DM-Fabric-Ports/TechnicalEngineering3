package ten3.util;

import java.util.function.Function;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class TransferUtil {

    public static <T> T execute(Function<Transaction, T> func) {
        try (Transaction t = getTransaction()) {
            return func.apply(t);
        }
    }

    @SuppressWarnings("deprecation")
    public static Transaction getTransaction() throws IllegalStateException {
        if (Transaction.isOpen()) {
            TransactionContext open = Transaction.getCurrentUnsafe();
            if (open != null)
                return open.openNested();
        }
        return Transaction.openOuter();
    }

}
