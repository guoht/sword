package net.guohaitao.sword;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by i@guohaitao.net on 14-9-11.
 * Description: 邮箱校验
 */
public final class Emails {


    private static final String SPECIAL_CHARS = "\\p{Cntrl}\\(\\)<>@,;:'\\\\\\\"\\.\\[\\]";
    private static final String VALID_CHARS = "[^" + SPECIAL_CHARS + "]";
    private static final String WORD = "(" + VALID_CHARS + ")+";

    private static final String LEGAL_ASCII_REGEX = "^\\p{ASCII}+$";
    private static final String EMAIL_REGEX = "^(.+)@(.+?)$";
    private static final String USER_REGEX = "^" + WORD + "(\\." + WORD + ")*$";

    private static final Pattern MATCH_ASCII_PATTERN = Pattern.compile(LEGAL_ASCII_REGEX);
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern USER_PATTERN = Pattern.compile(USER_REGEX);

    private Emails() {
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isValid(@Nullable String email) {
        if (Strings.isEmpty(email)) {
            return false;
        }
        Matcher asciiMatcher = MATCH_ASCII_PATTERN.matcher(email);
        if (!asciiMatcher.matches()) {
            return false;
        }
        // Check the whole email address structure
        Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        return emailMatcher.matches() &&
                !email.endsWith(".") &&
                USER_PATTERN.matcher(emailMatcher.group(1)).matches() &&
                Domain.isValid(emailMatcher.group(2));
    }

    private static class Domain {
        // Regular expression strings for hostnames (derived from RFC2396 and RFC 1123)
        private static final String DOMAIN_LABEL_REGEX = "\\p{Alnum}(?>[\\p{Alnum}-]*\\p{Alnum})*";
        private static final String TOP_LABEL_REGEX = "\\p{Alpha}{2,}";
        private static final String DOMAIN_NAME_REGEX = "^(?:" + DOMAIN_LABEL_REGEX + "\\.)+" + "(" + TOP_LABEL_REGEX + ")$";
        private static final Pattern DOMAIN_NAME_PATTERN = Pattern.compile(DOMAIN_NAME_REGEX);


        /**
         * 域名验证
         *
         * @param domain
         * @return
         */
        static boolean isValid(String domain) {
            Matcher matcher = DOMAIN_NAME_PATTERN.matcher(domain);
            if (matcher.matches()) {
                String topDomain = matcher.group(1);
                return isValidTld(topDomain);
            }

            return false;
        }

        /**
         * Returns true if the specified <code>String</code> matches any
         * IANA-defined top-level domain. Leading dots are ignored if present.
         * The search is case-sensitive.
         *
         * @param tld
         *         the parameter to check for TLD status
         * @return true if the parameter is a TLD
         */
        private static boolean isValidTld(String tld) {
            String iTld = tld.toLowerCase();
            return GENERIC_TLD_LIST.contains(chompLeadingDot(iTld)) ||
                    COUNTRY_CODE_TLD_LIST.contains(chompLeadingDot(iTld)) ||
                    INFRASTRUCTURE_TLD_LIST.contains(chompLeadingDot(iTld));

        }


        private static String chompLeadingDot(String str) {
            if (str.startsWith(".")) {
                return str.substring(1);
            } else {
                return str;
            }
        }

        // ---------------------------------------------
        // ----- TLDs defined by IANA
        // ----- Authoritative and comprehensive list at:
        // ----- http://data.iana.org/TLD/tlds-alpha-by-domain.txt

        private static final String[] INFRASTRUCTURE_TLDS = new String[]{
                "arpa",               // internet infrastructure
                "root"                // diagnostic marker for non-truncated root zone
        };

        private static final String[] GENERIC_TLDS = new String[]{
                "com",                // commercial enterprises
                "net",                // internet support infrastructure/business
                "org",                // noncommercial organizations
                "gov",                // United States Government
                "edu",                // accredited postsecondary US education entities
                "aero",               // air transport industry
                "asia",               // Pan-Asia/Asia Pacific
                "biz",                // businesses
                "cat",                // Catalan linguistic/cultural community
                "coop",               // cooperative associations
                "info",               // informational sites
                "jobs",               // Human Resource managers
                "mobi",               // mobile products and services
                "museum",             // museums, surprisingly enough
                "name",               // individuals' sites
                "pro",                // credentialed professionals and entities
                "tel",                // contact data for businesses and individuals
                "travel",             // entities in the travel industry
                "mil",                // United States Military
                "int"                 // organizations established by international treaty
        };

        private static final String[] COUNTRY_CODE_TLDS = new String[]{
                "cn",                 // China, mainland
                "hk",                 // Hong Kong
                "tw",                 // Taiwan, Republic of China
                "ac",                 // Ascension Island
                "ad",                 // Andorra
                "ae",                 // United Arab Emirates
                "af",                 // Afghanistan
                "ag",                 // Antigua and Barbuda
                "ai",                 // Anguilla
                "al",                 // Albania
                "am",                 // Armenia
                "an",                 // Netherlands Antilles
                "ao",                 // Angola
                "aq",                 // Antarctica
                "ar",                 // Argentina
                "as",                 // American Samoa
                "at",                 // Austria
                "au",                 // Australia (includes Ashmore and Cartier Islands and Coral Sea Islands)
                "aw",                 // Aruba
                "ax",                 // Åland
                "az",                 // Azerbaijan
                "ba",                 // Bosnia and Herzegovina
                "bb",                 // Barbados
                "bd",                 // Bangladesh
                "be",                 // Belgium
                "bf",                 // Burkina Faso
                "bg",                 // Bulgaria
                "bh",                 // Bahrain
                "bi",                 // Burundi
                "bj",                 // Benin
                "bm",                 // Bermuda
                "bn",                 // Brunei Darussalam
                "bo",                 // Bolivia
                "br",                 // Brazil
                "bs",                 // Bahamas
                "bt",                 // Bhutan
                "bv",                 // Bouvet Island
                "bw",                 // Botswana
                "by",                 // Belarus
                "bz",                 // Belize
                "ca",                 // Canada
                "cc",                 // Cocos (Keeling) Islands
                "cd",                 // Democratic Republic of the Congo (formerly Zaire)
                "cf",                 // Central African Republic
                "cg",                 // Republic of the Congo
                "ch",                 // Switzerland
                "ci",                 // Côte d'Ivoire
                "ck",                 // Cook Islands
                "cl",                 // Chile
                "cm",                 // Cameroon
                "co",                 // Colombia
                "cr",                 // Costa Rica
                "cu",                 // Cuba
                "cv",                 // Cape Verde
                "cx",                 // Christmas Island
                "cy",                 // Cyprus
                "cz",                 // Czech Republic
                "de",                 // Germany
                "dj",                 // Djibouti
                "dk",                 // Denmark
                "dm",                 // Dominica
                "do",                 // Dominican Republic
                "dz",                 // Algeria
                "ec",                 // Ecuador
                "ee",                 // Estonia
                "eg",                 // Egypt
                "er",                 // Eritrea
                "es",                 // Spain
                "et",                 // Ethiopia
                "eu",                 // European Union
                "fi",                 // Finland
                "fj",                 // Fiji
                "fk",                 // Falkland Islands
                "fm",                 // Federated States of Micronesia
                "fo",                 // Faroe Islands
                "fr",                 // France
                "ga",                 // Gabon
                "gb",                 // Great Britain (United Kingdom)
                "gd",                 // Grenada
                "ge",                 // Georgia
                "gf",                 // French Guiana
                "gg",                 // Guernsey
                "gh",                 // Ghana
                "gi",                 // Gibraltar
                "gl",                 // Greenland
                "gm",                 // The Gambia
                "gn",                 // Guinea
                "gp",                 // Guadeloupe
                "gq",                 // Equatorial Guinea
                "gr",                 // Greece
                "gs",                 // South Georgia and the South Sandwich Islands
                "gt",                 // Guatemala
                "gu",                 // Guam
                "gw",                 // Guinea-Bissau
                "gy",                 // Guyana
                "hm",                 // Heard Island and McDonald Islands
                "hn",                 // Honduras
                "hr",                 // Croatia (Hrvatska)
                "ht",                 // Haiti
                "hu",                 // Hungary
                "id",                 // Indonesia
                "ie",                 // Ireland (Éire)
                "il",                 // Israel
                "im",                 // Isle of Man
                "in",                 // India
                "io",                 // British Indian Ocean Territory
                "iq",                 // Iraq
                "ir",                 // Iran
                "is",                 // Iceland
                "it",                 // Italy
                "je",                 // Jersey
                "jm",                 // Jamaica
                "jo",                 // Jordan
                "jp",                 // Japan
                "ke",                 // Kenya
                "kg",                 // Kyrgyzstan
                "kh",                 // Cambodia (Khmer)
                "ki",                 // Kiribati
                "km",                 // Comoros
                "kn",                 // Saint Kitts and Nevis
                "kp",                 // North Korea
                "kr",                 // South Korea
                "kw",                 // Kuwait
                "ky",                 // Cayman Islands
                "kz",                 // Kazakhstan
                "la",                 // Laos (currently being marketed as the official domain for Los Angeles)
                "lb",                 // Lebanon
                "lc",                 // Saint Lucia
                "li",                 // Liechtenstein
                "lk",                 // Sri Lanka
                "lr",                 // Liberia
                "ls",                 // Lesotho
                "lt",                 // Lithuania
                "lu",                 // Luxembourg
                "lv",                 // Latvia
                "ly",                 // Libya
                "ma",                 // Morocco
                "mc",                 // Monaco
                "md",                 // Moldova
                "me",                 // Montenegro
                "mg",                 // Madagascar
                "mh",                 // Marshall Islands
                "mk",                 // Republic of Macedonia
                "ml",                 // Mali
                "mm",                 // Myanmar
                "mn",                 // Mongolia
                "mo",                 // Macau
                "mp",                 // Northern Mariana Islands
                "mq",                 // Martinique
                "mr",                 // Mauritania
                "ms",                 // Montserrat
                "mt",                 // Malta
                "mu",                 // Mauritius
                "mv",                 // Maldives
                "mw",                 // Malawi
                "mx",                 // Mexico
                "my",                 // Malaysia
                "mz",                 // Mozambique
                "na",                 // Namibia
                "nc",                 // New Caledonia
                "ne",                 // Niger
                "nf",                 // Norfolk Island
                "ng",                 // Nigeria
                "ni",                 // Nicaragua
                "nl",                 // Netherlands
                "no",                 // Norway
                "np",                 // Nepal
                "nr",                 // Nauru
                "nu",                 // Niue
                "nz",                 // New Zealand
                "om",                 // Oman
                "pa",                 // Panama
                "pe",                 // Peru
                "pf",                 // French Polynesia With Clipperton Island
                "pg",                 // Papua New Guinea
                "ph",                 // Philippines
                "pk",                 // Pakistan
                "pl",                 // Poland
                "pm",                 // Saint-Pierre and Miquelon
                "pn",                 // Pitcairn Islands
                "pr",                 // Puerto Rico
                "ps",                 // Palestinian territories (PA-controlled West Bank and Gaza Strip)
                "pt",                 // Portugal
                "pw",                 // Palau
                "py",                 // Paraguay
                "qa",                 // Qatar
                "re",                 // Réunion
                "ro",                 // Romania
                "rs",                 // Serbia
                "ru",                 // Russia
                "rw",                 // Rwanda
                "sa",                 // Saudi Arabia
                "sb",                 // Solomon Islands
                "sc",                 // Seychelles
                "sd",                 // Sudan
                "se",                 // Sweden
                "sg",                 // Singapore
                "sh",                 // Saint Helena
                "si",                 // Slovenia
                "sj",                 // Svalbard and Jan Mayen Islands Not in use (Norwegian dependencies; see .no)
                "sk",                 // Slovakia
                "sl",                 // Sierra Leone
                "sm",                 // San Marino
                "sn",                 // Senegal
                "so",                 // Somalia
                "sr",                 // Suriname
                "st",                 // São Tomé and Príncipe
                "su",                 // Soviet Union (deprecated)
                "sv",                 // El Salvador
                "sy",                 // Syria
                "sz",                 // Swaziland
                "tc",                 // Turks and Caicos Islands
                "td",                 // Chad
                "tf",                 // French Southern and Antarctic Lands
                "tg",                 // Togo
                "th",                 // Thailand
                "tj",                 // Tajikistan
                "tk",                 // Tokelau
                "tl",                 // East Timor (deprecated old code)
                "tm",                 // Turkmenistan
                "tn",                 // Tunisia
                "to",                 // Tonga
                "tp",                 // East Timor
                "tr",                 // Turkey
                "tt",                 // Trinidad and Tobago
                "tv",                 // Tuvalu
                "tz",                 // Tanzania
                "ua",                 // Ukraine
                "ug",                 // Uganda
                "uk",                 // United Kingdom
                "um",                 // United States Minor Outlying Islands
                "us",                 // United States of America
                "uy",                 // Uruguay
                "uz",                 // Uzbekistan
                "va",                 // Vatican City State
                "vc",                 // Saint Vincent and the Grenadines
                "ve",                 // Venezuela
                "vg",                 // British Virgin Islands
                "vi",                 // U.S. Virgin Islands
                "vn",                 // Vietnam
                "vu",                 // Vanuatu
                "wf",                 // Wallis and Futuna
                "ws",                 // Samoa (formerly Western Samoa)
                "ye",                 // Yemen
                "yt",                 // Mayotte
                "yu",                 // Serbia and Montenegro (originally Yugoslavia)
                "za",                 // South Africa
                "zm",                 // Zambia
                "zw",                 // Zimbabwe
        };
        private static final ImmutableList INFRASTRUCTURE_TLD_LIST = ImmutableList.builder().addAll(Arrays.asList(INFRASTRUCTURE_TLDS)).build();
        private static final ImmutableList GENERIC_TLD_LIST = ImmutableList.builder().addAll(Arrays.asList(GENERIC_TLDS)).build();
        private static final ImmutableList COUNTRY_CODE_TLD_LIST = ImmutableList.builder().addAll(Arrays.asList(COUNTRY_CODE_TLDS)).build();

    }
}
