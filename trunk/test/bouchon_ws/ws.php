<?php
require_once('./utils.php');
/**
 * SMACK verification functions
 *
 * This file implements all the functions required to handle
 * verification for SMACK
 *
 * @author Ammit Heeramun <ammit.heeramun@idsoft.mu>
 */

/**
 * Receiver verification function
 * 
 * @param String $receiverBIC
 * @param String $receiverIBAN
 * @return boolean
 */
function verifyReceiver($receiverBIC, $receiverIBAN){
    
    if(isReceiverBICAuth($receiverBIC)&&isReceiverQXBANAuth($receiverIBAN)) {
        return true;
    } else {}
    
    return false;
}

/**
 * Sender verification function
 * @param String $sender
 * @return boolean 
 */
function verifySender($senderBIC, $senderIBAN) {
    
    if(isSenderBicAuth($senderBIC)&&isValidQXBAN($senderIBAN, $senderBIC)&&!isQXBANBlackListed($senderIBAN)) {
        return true;
    } else {}
    return false;
}

/**
 * Correct date verification function
 * @param String $date
 * @return boolean
 */
function verifyDateCorrecte($date) {
    
    return  isDateCorrect($date);
}

/**
 * Date passed verification
 * @param String $date
 * @return boolean
 */
function verifyDatePassed($date) {
    
    return isDatePassed($date);
}

/**
 * Priority Verification
 * @param String $priority
 * @return boolean | string
 */
function verifyPriority($priority) {
    
    return isPriorityValid($priority);
}


// Set the value default value
$value = "An error has occurred";

// Check if we gave an action
if (isset($_GET["action"]))
{
    // Switch for the action
    switch ($_GET["action"])
    {
        case "verify_receiver":
          //var_dump($_GET);
          if (!isset($_GET["receiverBIC"])) {
              $value = "Missing argument receiver BIC";
              break;
          }
          else {}
          if (!isset($_GET["receiverIBAN"])) {
              $value = "Missing argument receiver IBAN";
              break;
          }
          else {}
          $value = verifyReceiver($_GET["receiverBIC"], $_GET["receiverIBAN"]);
          break;
        case "verify_sender":
          if (!isset($_GET["senderBIC"])) {
              $value = "Missing argument sender BIC";
              break;
          }
          else {}
          if (!isset($_GET["senderIBAN"])) {
              $value = "Missing argument sender IBAN";
              break;
          }
          else {}
          $value = verifySender($_GET["senderBIC"], $_GET["senderIBAN"]);
          break;
        case "verify_date_correct":
          if (isset($_GET["date"])) {
              $value = verifyDateCorrecte($_GET["date"]);
          }
          else {
              $value = "Missing argument date1";
          }
          break;
        case "verify_date_passed":
          if (isset($_GET["date"])) {
              $value = verifyDatePassed($_GET["date"]);
          }
          else {
              $value = "Missing argument date2";
          }
          break;
        case "verify_priority":
          if (isset($_GET["priority"])) {
              $value = verifyPriority($_GET["priority"]);
          }
          else {
              $value = "Missing argument priority";
          }
        break;
    }
}

exit(json_encode($value));
?>
