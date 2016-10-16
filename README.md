# call-recorder
Phone call recorder based on the Twilio platform.

The application enables to record and play back phone calls without a dedicated app. KSI keyless signature is used to timestamp and verify the integrity of recordings.

####Requirements  
* A [Twilio](http://www.twilio.com) account with a voice- and messaging-enabled phone number.
* An access to KSI via Catena. One option is to use tryout version of Catena provided by [Guardtime](https://guardtime.com/technology/blockchain-developers).

####Example use case
Let's say X wants to call Y and wants to record the call. X does not have a smartphone or (doesn't want to / can't) download a dedicated app for recording calls.

* X calls the voice-enabled Twilio number.
* X is prompted to enter the Y's phone number on the keypad and ending the phone number with an asterisk (*).
* The call is forwarded to Y.
* When Y picks up, s/he are notified that the call will be recorded, after which s/he will have 5 seconds to decline before the call is connected.
* If Y accepts the call and the call has ended, a text message with the URL of the recording is sent to X. The recording is signed with KSI. Signing time is included in the response text message.
* The recording can be played back by either:
  * Navigating to the URL directly
  * Forwarding the text message to the messaging-enabled Twilio number. After sending the text message, the sender will recevie a call and the recording will be played back. With this playback option, a check is performed to detect whether the recording has changed since it was last signed.


####Setup
* Point the Voice Request URL of the voice-enabled number to  
`http://deployed-app-url.com/recordCall/receive` (HTTP POST)
* Point the Messaging Request URL of the messaging-enabled number to  
`http://deployed-app-url.com/callPlayback` (HTTP POST)
* `cp config.sample.yaml config.yaml`
* Fill in `config.yaml` with the necessary configuration parameters
* `mvn clean package`
* `java -jar target/call-recorder-0.0.1-SNAPSHOT.jar server config.yaml`
* The server runs on port 8900 by default
