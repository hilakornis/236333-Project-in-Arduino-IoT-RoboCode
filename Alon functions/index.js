// [START import]
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// Retrieve Firebase Messaging object.
const messaging = admin.messaging();

exports.notifyCaptureImageReq = functions.firestore
    .document('requestMessages/{message}')
    .onCreate((docSnapshot, context) => {
        const message = docSnapshot.data();
        const senderName = message['senderName'];
		
		console.log('Hello, Im running for senderName ' + senderName);
 
        return admin.firestore().doc('Users/' + senderName).get().then(userDoc => {
            const registrationTokens = userDoc.get('registrationTokens')
            const notificationBody = message['text']
			// The topic name can be optionally prefixed with "/topics/".
			var topic = 'CaptureRequests_' + message['pairingCode'];

			var myMessage = {
			    data: {
                    title: senderName + ' sent you a message.',
                    message: message['senderId'],
                    pairingCode: message['pairingCode']
			    },
			    topic: topic
			};

			// Send a message to devices subscribed to the provided topic.
			return admin.messaging().send(myMessage)
			    .then((response) => {
			        // Response is a message ID string.
			        console.log('Successfully sent message:', response);
			        return admin.firestore().doc("users/" + senderName).update({
                        registrationTokens: registrationTokens
                    })
			    });
        })
    })