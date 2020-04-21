// [START import]
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp()
const spawn = require('child-process-promise').spawn;
const path = require('path');
const os = require('os');
const fs = require('fs');
// [END import]



// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, response) => {
response.send("Hello from Firebase!");
});


// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest(async (req, res) => {
  // Grab the text parameter.
  const original = req.query.text;
  // Push the new message into the Realtime Database using the Firebase Admin SDK.
  const snapshot = await admin.database().ref('/pre_pic').push({original: original});
  // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
  res.redirect(303, snapshot.ref.toString());
});


// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.makeUppercase = functions.database.ref('/pre_pic/{pushId}/original')
    .onCreate((snapshot, context) => {
      // Grab the current value of what was written to the Realtime Database.
      const original = snapshot.val();
      console.log('Uppercasing', context.params.pushId, original);
      const uppercase = original.toUpperCase();
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      return snapshot.ref.parent.child('uppercase').set(uppercase);
    });
	
	
// [START generateThumbnail]
/**
 * When an image is uploaded in the Storage bucket We generate a thumbnail automatically using
 * ImageMagick.
 */
// [START generateThumbnailTrigger]
exports.generateThumbnail = functions.storage.object().onFinalize(async (object) => {
// [END generateThumbnailTrigger]
	// [START eventAttributes]
	const fileBucket = object.bucket; // The Storage bucket that contains the file.
	const filePath = object.name; // File path in the bucket.
	const contentType = object.contentType; // File content type.
	const metageneration = object.metageneration; // Number of times metadata has been generated. New objects have a value of 1.
	// [END eventAttributes]

	// [START stopConditions]
	// Exit if this is triggered on a file that is not an image.
	if (!contentType.startsWith('image/')) {
	return console.log('This is not an image.');
	}

	// Get the file name.
	const fileName = path.basename(filePath);
	// Exit if the image is already a thumbnail.
	if (fileName.startsWith('thumb_')) {
	return console.log('Already a Thumbnail.');
	}
	// [END stopConditions]

	// [START thumbnailGeneration]
	// Download file from bucket.
	const bucket = admin.storage().bucket(fileBucket);
	const tempFilePath = path.join(os.tmpdir(), fileName);
	const metadata = {
	contentType: contentType,
	};
	await bucket.file(filePath).download({destination: tempFilePath});
	console.log('Image downloaded locally to', tempFilePath);
	// Generate a thumbnail using ImageMagick.
	await spawn('convert', [tempFilePath, '-thumbnail', '200x200>', tempFilePath]);
	console.log('Thumbnail created at', tempFilePath);
	// We add a 'thumb_' prefix to thumbnails file name. That's where we'll upload the thumbnail.
	const thumbFileName = `thumb_${fileName}`;
	const thumbFilePath = path.join(path.dirname(filePath), thumbFileName);
	// Uploading the thumbnail.
	await bucket.upload(tempFilePath, {
	destination: thumbFilePath,
	metadata: metadata,
	});
	// Once the thumbnail has been uploaded delete the local file to free up disk space.
	return fs.unlinkSync(tempFilePath);
	// [END thumbnailGeneration]
});
// [END generateThumbnail]
