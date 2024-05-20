/**
 * Copyright (c) Worldline 2017 - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 **/
package am.developer.camel.enumeration;

/**
 * Enumaration of All Terminal State
 *
 * @since 2.0.36
 **/
public enum State {

    /**
     * State NON_CONFIGURE, value 0 in Database
     **/
    NON_CONFIGURE,

    /**
     * State EN_SERVICE, value 1 in Database
     **/
    EN_SERVICE,

    /**
     * State DEGRADE, value 2 in Database
     **/
    DEGRADE,

    /**
     * State HORS_SERVICE, value 3 in Database
     **/
    HORS_SERVICE,

    /**
     * State MAINTENANCE, value 4 in Database
     */
    MAINTENANCE,

    /**
     * State ALERTE_PEU_GRAVE, value 5 in Database
     */
    ALERTE_PEU_GRAVE,

    /**
     * State ARRETE, value 6 in Database
     */
    ARRETE,

    /**
     * State INACTIF, value 7 in Database
     */
    INACTIF,

    /**
     * State DEMONTE, value 8 in Database
     */
    DEMONTE,

    /**
     * State PRET, value 9 in Database
     */
    PRET;

}
