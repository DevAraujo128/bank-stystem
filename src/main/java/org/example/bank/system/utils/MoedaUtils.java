package org.example.bank.system.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MoedaUtils {
    private MoedaUtils() {
    }

    public static String formatar(BigDecimal valor) {

        NumberFormat formato = NumberFormat.getCurrencyInstance(
                new Locale("pt", "BR"));

        return formato.format(valor);
    }
}
