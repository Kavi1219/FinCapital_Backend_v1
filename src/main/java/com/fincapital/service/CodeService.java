package com.fincapital.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CodeService {

    public String companyCode(String name) {

        String[] words = name
                .trim()
                .replaceAll(
                        "[^A-Za-z0-9 ]",
                        " "
                )
                .split("\\s+");

        if (words.length >= 2) {

            String c = Arrays
                    .stream(words)
                    .filter(
                            w -> !w.isBlank()
                    )
                    .limit(3)
                    .map(
                            w -> w
                                    .substring(0, 1)
                                    .toUpperCase()
                    )
                    .reduce(
                            "",
                            String::concat
                    );

            if (!c.isBlank()) {
                return c;
            }
        }

        String clean = name
                .replaceAll(
                        "[^A-Za-z0-9]",
                        ""
                )
                .toUpperCase();

        return clean.length() <= 3
                ? clean
                : clean.substring(0, 3);
    }

    public String customerCode(
            String c,
            long n
    ) {
        return c +
                "-" +
                String.format(
                        "%04d",
                        n
                );
    }

    public String loanCode(
            String c,
            long n
    ) {
        return c +
                "LN-" +
                String.format(
                        "%05d",
                        n
                );
    }

    public String employeeCode(
            String c,
            long n
    ) {
        return c +
                "EMP-" +
                String.format(
                        "%04d",
                        n
                );
    }

    public String paymentCode(
            String c,
            long n
    ) {
        return c +
                "PAY-" +
                String.format(
                        "%06d",
                        n
                );
    }

    public String expenseCode(
            String c,
            long n
    ) {
        return c +
                "EXP-" +
                String.format(
                        "%06d",
                        n
                );
    }

    public String transactionCode(
            String c,
            long n
    ) {
        return c +
                "TXN-" +
                String.format(
                        "%08d",
                        n
                );
    }

    public String branchCode(
            String c,
            long n
    ) {
        return c +
                "BR-" +
                String.format(
                        "%03d",
                        n
                );
    }

    // =========================================================
    // GET NUMBER FROM EXISTING TRANSACTION CODE
    //
    // Example:
    // SFCTXN-00000008 -> 8
    // =========================================================

    public long transactionNumber(
            String transactionCode
    ) {

        if (
                transactionCode == null ||
                        transactionCode.isBlank()
        ) {
            return 0;
        }

        int index =
                transactionCode
                        .lastIndexOf(
                                "TXN-"
                        );

        if (index < 0) {
            return 0;
        }

        String numberPart =
                transactionCode.substring(
                        index + 4
                );

        try {
            return Long.parseLong(
                    numberPart
            );
        } catch (
                NumberFormatException e
        ) {
            return 0;
        }
    }
}