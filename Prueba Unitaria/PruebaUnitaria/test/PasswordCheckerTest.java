import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordCheckerTest {

    PasswordChecker checker;

    @BeforeEach
    void setUp() {
        checker = new PasswordChecker();
        checker.setMinLength(8);
        checker.setMaxLength(12);
    }

    // Comprobaciones sobre PASSWORD_OK  --------------------

    @Test
    void passwordCorrecto() {
        int resultado = checker.check("abcde12345", "abcde12345");
        assertEquals(PasswordChecker.PASSWORD_OK, resultado);
    }

    // Comprobaciones sobre ERR_BLANK  --------------------

    @Test   // ERR_BLANK
    void passwordVacio() {
        int resultado = checker.check("", "");
        assertEquals(PasswordChecker.ERR_BLANK, resultado);
    }

    // Comprobaciones sobre ERR_LENGTH  --------------------

    @ParameterizedTest // Supuestos aciertos
    @ValueSource(strings = {"abcde123", "abcde1234", "abcde12345", "abcde123456", "abcde1234567"})
    void passwordLongitudAciertos(String password) {
        int resultado = checker.check(password, password);
        assertEquals(PasswordChecker.PASSWORD_OK, resultado);
    }

    @ParameterizedTest // Supuestos fallos                              ERROR !!!!
    @ValueSource(strings = {"abcde12", "abcde12345678"})
    void passwordLongitudFallos(String password) {
        int resultado = checker.check(password, password);
        assertEquals(PasswordChecker.ERR_LENGTH, resultado);
    }

    // Comprobaciones sobre ERR_DIGITS  --------------------

    @Test   //  Comprobación de fallo con password sin digitos
    void passwordDigitsFallo() {
        checker.setCheckDigits(true);
        int resultado = checker.check("abcdefgh", "abcdefgh");
        assertEquals(PasswordChecker.ERR_DIGITS, resultado);
    }

    @Test   //  Comprobación de aciertos con password con digitos
    void passwordDigitsAcierto() {
        checker.setCheckDigits(true);
        int resultado = checker.check("abcdefg1", "abcdefg1");
        assertEquals(PasswordChecker.PASSWORD_OK, resultado);
    }

    @Test   //  Comprobación con setCheckDigits en false
    void passwordDigitsAciertoCheckFalse() {
        checker.setCheckDigits(false);
        int resultado = checker.check("abcdefgh", "abcdefgh");
        assertEquals(PasswordChecker.PASSWORD_OK, resultado);
    }

    // Comprobaciones sobre ERR_LOWERS  --------------------

    @Test
    void passwordLowersAcierto() {
        checker.setCheckLowers(true);
        int resultado = checker.check("abcdefgh", "abcdefgh");
        assertEquals(PasswordChecker.PASSWORD_OK, resultado);
    }

    @Test
    void passwordLowersFallo() {
        checker.setCheckLowers(true);
        int resultado = checker.check("ABCDEFGH", "ABCDEFGH");
        assertEquals(PasswordChecker.ERR_LOWERS, resultado);
    }

    @Test
    void passwordLowersAciertoCheckFalse() {
        checker.setCheckLowers(false);
        int resultado = checker.check("ABCDEFGH", "ABCDEFGH");
        assertEquals(PasswordChecker.PASSWORD_OK, resultado);
    }

    // Comprobaciones sobre ERR_UPPERS  --------------------

    @Test
    void passwordUppersAcierto() {
        checker.setCheckUppers(true);
        int resultado = checker.check("ABCDEFGH", "ABCDEFGH");
        assertEquals(PasswordChecker.PASSWORD_OK, resultado);
    }

    @Test
    void passwordUppersFallo() {
        checker.setCheckUppers(true);
        int resultado = checker.check("abcdefgh", "abcdefgh");
        assertEquals(PasswordChecker.ERR_UPPERS, resultado);
    }

    @Test
    void passwordUppersAciertoCheckFalse() {
        checker.setCheckUppers(false);
        int resultado = checker.check("abcdefgh", "abcdefgh");
        assertEquals(PasswordChecker.PASSWORD_OK, resultado);
    }

    // Comprobaciones sobre ERR_NO_MATCH  --------------------

    @Test
    void passwordNoMatchFallo() {
        int resultado = checker.check("abcdefgh", "abcdefgh12");
        assertEquals(PasswordChecker.ERR_NO_MATCH, resultado);
    }



    // --------------------------------------------------------



    // Comprobaciones sobre setMinLength / getMinLength  --------------------

    @Test   // Un valor válido se guarda
    void setMinLengthGuardaElValor() {
        checker.setMinLength(5);
        assertEquals(5, checker.getMinLength());
    }

    @Test   // Límite: 0 no es inferior a cero, así que debe ser válido
    void setMinLengthCeroEsValido() {
        assertDoesNotThrow(() -> checker.setMinLength(0));
        assertEquals(0, checker.getMinLength());
    }

    @Test   // Límite: -1 es inferior a cero, debe lanzar excepción
    void setMinLengthNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> checker.setMinLength(-1));
    }

    @Test   // La excepción debe llevar el mensaje documentado
    void setMinLengthNegativoMensaje() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> checker.setMinLength(-1));
        assertEquals(PasswordChecker.ERR_INVALID_MIN_LENGTH, e.getMessage());
    }

    // Comprobaciones sobre setMaxLength / getMaxLength  --------------------
    // (setMaxLengthGuardaElValor ya lo tienes)

    @Test   // Límite: igual que el mínimo (8), debe ser válido
    void setMaxLengthIgualAlMinimoEsValido() {
        assertDoesNotThrow(() -> checker.setMaxLength(8));
        assertEquals(8, checker.getMaxLength());
    }

    @Test   // Límite: mínimo - 1 (7), debe lanzar excepción
    void setMaxLengthMenorQueMinimoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> checker.setMaxLength(7));
    }

    @Test   // La excepción debe llevar el mensaje documentado
    void setMaxLengthMenorQueMinimoMensaje() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> checker.setMaxLength(7));
        assertEquals(PasswordChecker.ERR_INVALID_MAX_LENGTH, e.getMessage());
    }

    // Comprobaciones sobre getters y setters booleanos  --------------------

    @Test   // El JavaDoc dice que el valor por defecto es false
    void comprobacionesPorDefectoDesactivadas() {
        PasswordChecker nuevo = new PasswordChecker();
        assertFalse(nuevo.getCheckDigits());
        assertFalse(nuevo.getCheckUppers());
        assertFalse(nuevo.getCheckLowers());
    }

    @Test
    void setCheckDigitsSeReflejaEnElGetter() {
        checker.setCheckDigits(true);
        assertTrue(checker.getCheckDigits());
        checker.setCheckDigits(false);
        assertFalse(checker.getCheckDigits());
    }

    @Test
    void setCheckUppersSeReflejaEnElGetter() {
        checker.setCheckUppers(true);
        assertTrue(checker.getCheckUppers());
        checker.setCheckUppers(false);
        assertFalse(checker.getCheckUppers());
    }

    @Test
    void setCheckLowersSeReflejaEnElGetter() {
        checker.setCheckLowers(true);
        assertTrue(checker.getCheckLowers());
        checker.setCheckLowers(false);
        assertFalse(checker.getCheckLowers());
    }
}


