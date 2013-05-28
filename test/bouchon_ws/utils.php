<?php
/**
 * SMACK utils functions
 *
 * This file implements all the functions required in the 
 * SMACK Verfication Web Services
 *
 * @author Ammit Heeramun <ammit.heeramun@idsoft.mu>
 */

/**
 * Check whether the parameterized BIC is found in the authorized list
 * @param type $bic
 * @return boolean Whether the parameterized BIC is found in the authorized list
 */
function isReceiverBICAuth($bic) {
    
    //Read the config file
    $configBIC = parse_ini_file('./config/configRcvBIC.ini');
    
    // Loop in the list to check if the parameterized BIC matches a value in the list
    foreach ($configBIC["bicList"] as &$bicValue) {
        if($bic == $bicValue) {
            return true;
        }
    }
    return false;
}

/**
 * Check whether the parameterized QXBAN is found in the authorized list
 * @param type $qxban
 * @return boolean Whether the parameterized QXBAN is found in the authorized list
 */
function isReceiverQXBANAuth($qxban) {
    //Read the config file
    $configQXBAN = parse_ini_file('./config/configRcvQXBAN.ini');
    
    // Loop in the list to check if the parameterized QXBAN matches a value in the list
    foreach ($configQXBAN["qxbanList"] as &$qxbanValue) {
        if($qxban == $qxbanValue) {
            return true;
        }
    }
    return false;
}

/**
 * Check whether the parameterized BIC is found in the authorized list
 * @param type $bic
 * @return boolean Whether the parameterized BIC is found in the authorized list
 */
function isSenderBicAuth($bic) {
    
    //Read the config file
    $configBIC = parse_ini_file('./config/configSndBIC.ini');
    
    // Loop in the list to check if the parameterized BIC matches a value in the list
    foreach ($configBIC["bicList"] as &$bicValue) {
        if($bic == $bicValue) {
            return true;
        }
    }
    return false;
}

/**
 * Check whether the parameterized QXBAN is valid with regard to the SEPAmail documentation
 * 
 * @see http://documentation.sepamail.eu/wiki/Standards:Algorithme_de_g%C3%A9n%C3%A9ration_du_QXBAN
 * @param String $qxban
 * @param String $bic
 * @return boolean Whether the parameterized QXBAN is valid with regard to the SEPAmail documentation
 */
function isValidQXBAN($qxban,$bic) {
    
    // Check the lenght of the QXBAN it should be 34 characters long
    if(34 != strlen(trim($qxban))) {
        return false;
    } else {}
    
    // If the lenght of the BIC is eight characters concatenate "XXX" to it
    if(8 == strlen($bic)) {
        $bic = $bic."XXX";
    }
    
    // Check if we have the 'QX' in the place of the first two characters,
    // two digits in the next two characters, the next eleven characters is the BIC 
    // and numbers and uppercase letters in the place of the remaining characters
    if(!preg_match("^QX\d{2}BICVERT1XXX[A-Z0-9]*$^", $qxban)) {
        return false;
    } else {}
    
    return true;
}

/**
 * Check whether parametered QXBAN is black listed
 * 
 * @param type $qxban
 * @return boolean Whether parametered QXBAN is black listed
 */
function isQXBANBlackListed($qxban) {
    //Read the config file
    $configQXBAN = parse_ini_file('./config/configSndBlackListQxban.ini');
    
    // Loop in the list to check if the parameterized QXBAN matches a value in the list
    foreach ($configQXBAN["qxbanList"] as &$qxbanValue) {
        if($qxban == $qxbanValue) {
            return true;
        }
    }
    return false;
}

/**
 * Check whether date is correcte
 * 
 * @param type $date
 * @return boolean whether date is correcte
 */
function isDateCorrect($date) {
    
    //H1
    $sendDate = new DateTime($date);
    $sendISODate = $sendDate->format(DateTime::ISO8601);
    
    //H2
    $currentDate = new DateTime();
    $currentISODate = $currentDate->format(DateTime::ISO8601);
    $newCurrentDate = date("c", strtotime($currentISODate)+3);
    
    if($sendISODate < $currentISODate) {
        return true;
    }
    
    if($sendISODate > $currentISODate  && $sendISODate < $newCurrentDate) {
        return true;
    }
    
    return false;
}

/**
 * Check whether date is passed
 * 
 * @param type $date
 * @return boolean Whether date is passed
 */
function isDatePassed($date) {
    
    //H1
    $sendDate = new DateTime($date);
    $sendISODate = $sendDate->format(DateTime::ISO8601);
    
    //H2
    $currentDate = new DateTime();
    $currentISODate = $currentDate->format(DateTime::ISO8601);
    
    if($currentISODate > $sendISODate) {
        return true;
    }
    
    return false;
}

/**
 * Check whether the priority is valid else return the default priority
 * 
 * @param type $priority
 * @return boolean | string
 */
function isPriorityValid($priority){
    //Read the config file
    $configPriority = parse_ini_file('./config/configPriority.ini');
    
    // Loop in the list to check if the parameterized Priority matches a value in the list
    foreach ($configPriority["priorityList"] as &$priorityValue) {
        if($priority == $priorityValue) {
            return true;
        }
    }
    return $configPriority["priorityDefault"];
}

?>
