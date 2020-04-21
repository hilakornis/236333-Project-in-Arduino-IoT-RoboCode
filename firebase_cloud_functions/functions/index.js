// [START import]
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const spawn = require('child-process-promise').spawn;
const path = require('path');
const os = require('os');
const fs = require('fs');
const mkdirp = require('mkdirp');
const gcs = require('@google-cloud/storage');
//const mkdirp = require('mkdirp-promise');
// [END import]

// Thumbnail prefix added to file names.
const croped_PREFIX = 'croped_';




// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, response) => {
    response.send("Hello from Firebase!");
});


// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest(async(req, res) => {
    // Grab the text parameter.
    const original = req.query.text;
    // Push the new message into the Realtime Database using the Firebase Admin SDK.
    const snapshot = await admin.database().ref('/pre_pic').push({ original: original });
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



exports.generateCropedImage = functions.storage.object().onFinalize(async(object) => {
    // File and directory paths.
    const filePath = object.name;
    const contentType = object.contentType; // This is the image MIME type
    const fileDir = path.dirname(filePath);
    const fileName = path.basename(filePath);
    const cropedFilePath = path.normalize(path.join(fileDir, `${croped_PREFIX}${fileName}`));
    const tempLocalFile = path.join(os.tmpdir(), filePath);
    const tempLocalDir = path.dirname(tempLocalFile);
    const tempLocalCropedFile = path.join(os.tmpdir(), cropedFilePath);
    const crop = "1500x650+0+0";
    const size = "1500x650^";

    // Exit if this is triggered on a file that is not an image.
    if (!contentType.startsWith('image/')) {
        return console.log('This is not an image.');
    }

    // Exit if the image is already a croped.
    if (fileName.startsWith('croped_')) {
        return console.log('Already a croped pic.');
    }

    // Cloud Storage files.
    const bucket = admin.storage().bucket(object.bucket);
    const file = bucket.file(filePath);
    const cropedFile = bucket.file(cropedFilePath);
    const metadata = {
        contentType: contentType,
        // To enable Client-side caching you can set the Cache-Control headers here. Uncomment below.
        // 'Cache-Control': 'public,max-age=3600',
    };

    // Create the temp directory where the storage file will be downloaded.
    await mkdirp(tempLocalDir)
        // Download file from bucket.
    await file.download({ destination: tempLocalFile });
    console.log('The file has been downloaded to', tempLocalFile);

    // Generate a croped using ImageMagick.
    await spawn('convert', [tempLocalFile, '-geometry', size, '-gravity', 'center', '-crop', crop, tempLocalCropedFile], {
        capture: ['stdout', 'stderr']
    });
    await spawn('convert', [tempLocalFile, '-strip', '-interlace', 'Plane', '-quality', '90', tempLocalFile]);

    console.log('croped image created at', tempLocalCropedFile);
    // Uploading the croped.
    await bucket.upload(tempLocalCropedFile, { destination: cropedFilePath, metadata: metadata });
    console.log('croped uploaded to Storage at', cropedFilePath);
    // Once the image has been uploaded delete the local files to free up disk space.
    fs.unlinkSync(tempLocalFile);
    fs.unlinkSync(tempLocalCropedFile);
    // Get the Signed URLs for the croped and original image.
    const config = {
        action: 'read',
        expires: '03-01-2500',
    };
    const results = await Promise.all([
        cropedFile.getSignedUrl(config),
        file.getSignedUrl(config),
    ]);
    console.log('Got Signed URLs.');
    const cropedResult = results[0];
    const originalResult = results[1];
    const cropedFileUrl = cropedResult[0];
    const fileUrl = originalResult[0];
    // Add the URLs to the Database
    await admin.database().ref('images').push({ path: fileUrl, croped: cropedFileUrl });
    return console.log('croped URLs saved to database.');










    // // [END generateThumbnailTrigger]
    // // [START eventAttributes]
    // const fileBucket = object.bucket; // The Storage bucket that contains the file.
    // const filePath = object.name; // File path in the bucket.
    // const fileName = path.basename(filePath);
    // const contentType = object.contentType; // File content type.
    // const metageneration = object.metageneration; // Number of times metadata has been generated. New objects have a value of 1.
    // const crop = "1500x650+0+0";
    // const size = "1500x650^";
    // // [END eventAttributes]

    // // [START stopConditions]
    // // Exit if this is triggered on a file that is not an image.
    // if (!contentType.startsWith('image/')) {
    //     return console.log('This is not an image.');
    // }

    // if (fileName.startsWith('croped_')) {
    //     return console.log('Already a croped.');
    // }

    // // Exit if the image is already a thumbnail.

    // // [END stopConditions]

    // // [START thumbnailGeneration]
    // // Download file from bucket.
    // const bucket = admin.storage().bucket(fileBucket);
    // const tempFilePath = path.join(os.tmpdir(), fileName);
    // const metadata = {
    //     contentType: contentType,
    // };
    // await bucket.file(filePath).download({ destination: tempFilePath });
    // console.log('Image downloaded locally to', tempFilePath);
    // // Generate a thumbnail using ImageMagick.
    // await spawn('convert', [tempFilePath, '-geometry', size, '-gravity', 'center', '-crop', crop, tempFilePath]);
    // console.log('Thumbnail created at', tempFilePath);
    // // We add a 'thumb_' prefix to thumbnails file name. That's where we'll upload the thumbnail.
    // const thumbFileName = `croped_${fileName}`;
    // const thumbFilePath = path.join(path.dirname(filePath), thumbFileName);
    // // Uploading the thumbnail.
    // await bucket.upload(tempFilePath, {
    //     destination: thumbFilePath,
    //     metadata: metadata,
    // });
    // // Once the thumbnail has been uploaded delete the local file to free up disk space.
    // return fs.unlinkSync(tempFilePath);
    // // [END thumbnailGeneration]
});
// [END generateThumbnail]