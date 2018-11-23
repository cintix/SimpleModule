/*
 */
module Logging {
    provides java.lang.System.LoggerFinder
            with dk.cintix.servermodule.logging.LoggerFinder;
    exports dk.cintix.servermodule.logging;
}
