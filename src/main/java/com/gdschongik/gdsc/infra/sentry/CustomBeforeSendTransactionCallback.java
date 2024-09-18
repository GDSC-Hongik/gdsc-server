package com.gdschongik.gdsc.infra.sentry;

import static com.gdschongik.gdsc.global.common.constant.SentryConstant.*;

import io.sentry.Hint;
import io.sentry.SentryOptions;
import io.sentry.protocol.SentryTransaction;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class CustomBeforeSendTransactionCallback implements SentryOptions.BeforeSendTransactionCallback {

    @Override
    public SentryTransaction execute(@NotNull SentryTransaction transaction, @NotNull Hint hint) {
        String transactionEndpoint = transaction.getTransaction();

        if (transactionEndpoint == null) {
            return transaction;
        }

        if (KEYWORDS_TO_IGNORE.stream().anyMatch(transactionEndpoint::contains)) {
            return null;
        }

        return transaction;
    }
}
