// [START import]
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const spawn = require('child-process-promise').spawn;
const path = require('path');
const os = require('os');
const fs = require('fs');
const mkdirp = require('mkdirp');
const XMLHttpRequest = require('xhr');

const { v4: uuidv4 } = require('uuid');
const uuid = uuidv4();

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
    const snapshot = await admin.database().ref('/messages').push({ original: original });
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    res.redirect(303, snapshot.ref.toString());
});


// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
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


function formatParams(params) {
    return "?" + Object
        .keys(params)
        .map((key) => {
            return key + "=" + encodeURIComponent(params[key])
        })
        .join("&")
}

exports.generateCropedImage = functions.storage.object().onFinalize(async(object) => {
    // File and directory paths.
    const filePath = object.name;
    const contentType = object.contentType; // This is the image MIME type
    const fileDir = path.dirname(filePath);
    const fileName = path.basename(filePath);

    const tempLocalFile = path.join(os.tmpdir(), filePath);
    const tempLocalDir = path.dirname(tempLocalFile);

    var QR_grid = ["100X100+0+0", "100X100+120+0", "100X100+0+120", "100X100+110+110"];


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

    const metadata = {
        contentType: contentType,
        metadata: {
            firebaseStorageDownloadTokens: uuid
        },
        // To enable Client-side caching you can set the Cache-Control headers here. Uncomment below.
        'Cache-Control': 'public,max-age=3600'
    };

    // Create the temp directory where the storage file will be downloaded.
    await mkdirp(tempLocalDir)
        // Download file from bucket.
    await file.download({ destination: tempLocalFile });
    console.log('The file has been downloaded to', tempLocalFile);

    var i = 0;
    promises = [];
    const temp_crop_pic_location = [];
    const final_pic_location = []
    QR_grid.forEach(QR_position => {
        cropedFilePath = path.normalize(path.join(fileDir, `${croped_PREFIX}${i}_${fileName}`));
        current_location = path.join(os.tmpdir(), cropedFilePath);

        final_pic_location.push(cropedFilePath)
        temp_crop_pic_location.push(current_location);

        // Generate a croped using ImageMagick.
        const p = spawn('convert', [tempLocalFile, '-gravity', 'North-West', '-crop', QR_position, current_location], {
            capture: ['stdout', 'stderr']
        });
        //await spawn('convert', [tempLocalFile, '-strip', '-interlace', 'Plane', '-quality', '0', tempLocalFile]);
        promises.push(p);
        console.log('croped image created at', current_location);
        i++;
    });

    // Wait for all pictures to be croped 
    await Promise.all(promises)

    promises = [];
    i = 0;

    // upload pics from temp location to directory
    temp_crop_pic_location.forEach(current_temp_location => {
        const p = bucket.upload(current_temp_location, { destination: final_pic_location[i], uploadType: "media", metadata: metadata });
        promises.push(p)
        console.log('croped uploaded to Storage at', final_pic_location[i]);
        i++;
    })

    // Wait for all croped pics to be uploaded
    await Promise.all(promises)

    // Once the image has been uploaded delete the local files to free up disk space.
    temp_crop_pic_location.forEach(current_temp_location => {
        fs.unlinkSync(current_temp_location);
    })
    fs.unlinkSync(tempLocalFile);


    var endpoint = "https://us-central1-hw3-b807b.cloudfunctions.net/addMessage"
    var params = {
        texe: "I'm alive!!"
    }

    var http = new XMLHttpRequest();
    var url = endpoint + formatParams(params)
    console.log('Calling http function');

    http.open("GET", url, false);
    // http.onreadystatechange = function() {
    //     if (http.readyState == 4 && http.status == 200) {
    //         alert(http.responseText);
    //     }
    // }
    http.send(null);
    // var url = "https://us-central1-hw3-b807b.cloudfunctions.net/addMessage"
    // var params = "rwq=hibaby"
    // var http = new XMLHttpRequest();
    // http.open("GET", url + "?" + params, false);
    // http.send(null);
    // this.addMessage("test");


    return console.log('Function done');

});