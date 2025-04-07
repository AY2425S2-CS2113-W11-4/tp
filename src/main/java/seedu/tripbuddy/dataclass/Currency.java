package seedu.tripbuddy.dataclass;

/**
 * Represents supported currencies and their exchange rates relative to the base currency (default: SGD).
 * Each enum constant has a currency code and a conversion rate.
 */
public enum Currency {
    MYR("MYR", 3.29),
    PHP("PHP", 42.63),
    SGD("SGD", 1),
    THB("THB", 25.52),
    USD("USD", 0.74),
    EUR("EUR", 0.68),
    JPY("JPY", 109.11),
    AUD("AUD", 1.23),
    CAD("CAD", 1.06),
    CNY("CNY", 5.41),
    HKD("HKD", 5.77),
    INR("INR", 63.50),
    NZD("NZD", 1.33),
    CHF("CHF", 0.64),
    TWD("TWD", 24.63),
    ZAR("ZAR", 14.18),
    GBP("GBP", 0.58);

    private final String name;
    private double rate;

    /**
     * Constructs a currency enum with the given name and rate.
     *
     * @param name the currency code (e.g. \"USD\")
     * @param rate the exchange rate relative to the base currency
     */
    Currency(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    /**
     * Returns the currency code.
     *
     * @return the full currency code (e.g. \"SGD\")
     */
    public String getFullName() {
        return name;
    }

    /**
     * Converts an amount from the base currency to this currency.
     *
     * @param amount the amount in base currency
     * @return the converted amount in this currency
     */
    public double convert(double amount) {
        return amount * rate;
    }

    /**
     * Returns the current exchange rate of this currency relative to the base currency.
     *
     * @return the exchange rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * Updates the exchange rate of this currency.
     *
     * @param rate the new exchange rate to set
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * Sets a new base currency by recalculating all exchange rates relative to the given base.
     *
     * @param newBase the new base currency
     */
    public static void setBaseCurrency(Currency newBase) {
        double newBaseRate = newBase.rate;
        for (Currency c : Currency.values()) {
            c.rate /= newBaseRate;
        }
    }

    /**
     * Returns a formatted string of the given amount in this currency.
     *
     * @param amount the numeric amount
     * @return a string formatted as \"#.## CODE\" (e.g. \"10.50 USD\")
     */
    public String getFormattedAmount(double amount) {
        return String.format("%.2f", amount) + " " + name;
    }
}
