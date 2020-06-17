// [START import]
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();
const spawn = require('child-process-promise').spawn;
const path = require('path');
const os = require('os');
const fs = require('fs');
const mkdirp = require('mkdirp');

const { v4: uuidv4 } = require('uuid');
const uuid = uuidv4();

const gcs = require('@google-cloud/storage');
//const mkdirp = require('mkdirp-promise');

const jpeg = require("jpeg-js");
const jsqr_1 = require("jsqr");


const jsQR = require("jsqr");

var db = admin.database();
// var ref = db.ref("server/saving-data/fireblog");
var ref = db.ref("users");



// [END import]

// Thumbnail prefix added to file names.
const croped_PREFIX = 'croped_';

// Retrieve Firebase Messaging object.
const messaging = admin.messaging();

// // Create a root reference
// var storageRef = firebase.storage().ref();



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

            // Alon ::
            // Here we build the message that will be sent to the capturing device that has
            // the paring code message['pairingCode'].
            // This device is subscribed to the topic 'CaptureRequests_' + message['pairingCode'],
            // so by sending this message to this topic, it will receive it.
            //
            // Hila, you should send the result in your function to the topic:
            //      'captured_' + message['pairingCode']
            // which will also be the name of the captured image file uploaded to the server and
            // given to shahaf's function.
            var myMessage = {
                data: {
                    title: senderName + ' sent you a message.',
                    message: message['senderId'],
                    pairingCode: message['pairingCode']

                },
                topic: topic
            };

            // Alon ::
            // Here we send the message to devices subscribed to the provided topic.
            return admin.messaging().send(myMessage)
                .then((response) => {
                    // Response is a message ID string.
                    // Here we can add a log to see how the sending went
                    console.log('Successfully sent message:', response);
                    return admin.firestore().doc("users/" + senderName).update({
                        registrationTokens: registrationTokens
                    })
                });

        })
    })

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


//exports.sendEmailNotification = functions.testLab.testMatrix().onComplete((testMatrix) =>


exports.newGenerateCropedImage = functions.https.onRequest(async(req, res) => {
    const filePath = req.query.text
    console.log('file path is: ' + filePath)
    const bucket = admin.storage().bucket();
    const file = bucket.file(filePath);
    const fileDir = path.dirname(filePath);
    const fileName = path.basename(filePath);

    const tempLocalFile = path.join(os.tmpdir(), filePath);
    const tempLocalDir = path.dirname(tempLocalFile);

    var basic_qr_size = "375X390";
    var go_down = 945;
    var go_left = 88;

    //var QR_grid = ["385X392+88+950", "100X100+120+0", "100X100+0+120", "100X100+110+110"];


    // Exit if this is triggered on a file that is not an image.
    if (!file.contentType.startsWith('image/')) {
        return console.log('This is not an image.');
    }

    // Exit if the image is already a croped.
    if (fileName.startsWith('croped_')) {
        return console.log('Already a croped pic.');
    }

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
    const final_pic_location = [];
    var line = [5, 6];
    var colume = [0, 1, 2, 3, 4, 5];
    line.forEach(QR_line => {
        colume.forEach(QR_coulume => {
            cropedFilePath = path.normalize(path.join(fileDir, `${croped_PREFIX}${i}_${fileName}`));
            current_location = path.join(os.tmpdir(), cropedFilePath);

            final_pic_location.push(cropedFilePath)
            temp_crop_pic_location.push(current_location);

            // Generate a croped using ImageMagick.
            const p = spawn('convert', [tempLocalFile, '-gravity', 'North-West', '-crop', basic_qr_size + "+" +
                (go_left + 480 * QR_coulume) + "+" + (go_down + 470 * QR_line), current_location
            ], {
                capture: ['stdout', 'stderr']
            });
            //await spawn('convert', [tempLocalFile, '-strip', '-interlace', 'Plane', '-quality', '0', tempLocalFile]);
            promises.push(p);
            console.log('croped image created at', current_location);
            i++;
        });
    });

    await Promise.all(promises)

    promises = [];
    i = 0;

    // resize all pictures


    temp_crop_pic_location.forEach(current_temp_location => {
        const p = spawn('convert', [current_temp_location, '-resize', '100x100', current_temp_location], {
            capture: ['stdout', 'stderr']
        });
        promises.push(p);
        console.log('resized picture at ', current_location);
        i++;
    })

    // Wait for all pictures to be croped 
    await Promise.all(promises)

    promises = [];
    i = 0;

    const width = 100; //in pixels
    const height = 100; //in pixels

    const Uint8ClampedArray = require('typedarray').Uint8ClampedArray;
    let jpegData, rawImageData, code

    temp_crop_pic_location.forEach(current_temp_location => {
        jpegData = fs.readFileSync(current_temp_location);
        rawImageData = jpeg.decode(jpegData);
        let clampedArray = new Uint8ClampedArray(rawImageData.data.length);
        let j;
        for (j = 0; j < rawImageData.data.length; j++) {
            clampedArray[j] = rawImageData.data[j];
        }
        code = jsqr_1.default(clampedArray, width, height);

        if (code) {
            console.log("Found QR code", code);

            var original_pic_name = fileName.substring(0, fileName.length - 4)
            console.log('This is the original pic name: ' + original_pic_name);

            var usersRef = ref.child(original_pic_name);
            usersRef.child(fileName.substring(0, fileName.length - 4)).set({
                fileName: fileName,
                TypeMessage: "QR code is",
                QR_Code: code.data,
                Index: i //todo later change
            });
        } else {
            console.log("Not found any");
        }
        i++;
    })


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
    // temp_crop_pic_location.forEach(current_temp_location => {
    //     fs.unlinkSync(current_temp_location);
    // })
    // fs.unlinkSync(tempLocalFile);

    return console.log('Function done');


    // Starting reading QR codes

});

// exports.newQrReader = functions.onComplete((newGenerateCropedImage) => {
//     console.log('STARTED QR READER!!!');
//     const final_pic_location = newGenerateCropedImage.final_pic_location;

//     final_pic_location.forEach(location => {
//         console.log("location " + i + "in " + location);
//     })
// });




exports.generateCropedImage = functions.storage.object().onFinalize(async(object) => {
    // File and directory paths.
    const filePath = object.name;
    const contentType = object.contentType; // This is the image MIME type
    const fileDir = path.dirname(filePath);
    const fileName = path.basename(filePath);

    const tempLocalFile = path.join(os.tmpdir(), filePath);
    const tempLocalDir = path.dirname(tempLocalFile);

    var basic_qr_size = "375X390";
    var go_down = 945;
    var go_left = 88;

    //var QR_grid = ["385X392+88+950", "100X100+120+0", "100X100+0+120", "100X100+110+110"];


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
    const final_pic_location = [];
    var line = [5, 6];
    var colume = [0, 1, 2, 3, 4, 5];
    line.forEach(QR_line => {
        colume.forEach(QR_coulume => {
            cropedFilePath = path.normalize(path.join(fileDir, `${croped_PREFIX}${i}_${fileName}`));
            current_location = path.join(os.tmpdir(), cropedFilePath);

            final_pic_location.push(cropedFilePath)
            temp_crop_pic_location.push(current_location);

            // Generate a croped using ImageMagick.
            const p = spawn('convert', [tempLocalFile, '-gravity', 'North-West', '-crop', basic_qr_size + "+" +
                (go_left + 480 * QR_coulume) + "+" + (go_down + 470 * QR_line), current_location
            ], {
                capture: ['stdout', 'stderr']
            });
            //await spawn('convert', [tempLocalFile, '-strip', '-interlace', 'Plane', '-quality', '0', tempLocalFile]);
            promises.push(p);
            console.log('croped image created at', current_location);
            i++;
        });
    });

    await Promise.all(promises)

    // promises = [];
    // i = 0;

    // // resize all pictures
    // temp_crop_pic_location.forEach(current_temp_location => {
    //     const p = spawn('convert', [current_temp_location, '-resize', '100x100', current_temp_location], {
    //         capture: ['stdout', 'stderr']
    //     });
    //     promises.push(p);
    //     console.log('resized picture at ', current_location);
    //     i++;
    // })

    // Wait for all pictures to be croped 
    await Promise.all(promises)

    promises = [];
    i = 0;

    const width = 375; //in pixels
    const height = 390; //in pixels


    const Uint8ClampedArray = require('typedarray').Uint8ClampedArray;
    MAX_ARRAY_LENGTH = 2000000;

    temp_crop_pic_location.forEach(current_temp_location => {

        let jpegData = fs.readFileSync(current_temp_location);
        let rawImageData = jpeg.decode(jpegData);
        let clampedArray = new Uint8ClampedArray(rawImageData.data.length);
        let j;
        for (j = 0; j < rawImageData.data.length; j++) {
            clampedArray[j] = rawImageData.data[j];
        }

        const code = jsQR(clampedArray, width, height);

        if (code) {
            console.log("Found QR code in block " + i + " ", code);
        } else {
            console.log("Couldn't find QR code in block " + i);
        }
        i++;
    })

    i = 0;

    // // upload pics from temp location to directory
    // temp_crop_pic_location.forEach(current_temp_location => {
    //     const p = bucket.upload(current_temp_location, { destination: final_pic_location[i], uploadType: "media", metadata: metadata });
    //     promises.push(p)
    //     console.log('croped uploaded to Storage at', final_pic_location[i]);
    //     i++;
    // })

    // // Wait for all croped pics to be uploaded
    // await Promise.all(promises)

    // Once the image has been uploaded delete the local files to free up disk space.
    // temp_crop_pic_location.forEach(current_temp_location => {
    //     fs.unlinkSync(current_temp_location);
    // })
    // fs.unlinkSync(tempLocalFile);

    return console.log('Function done');

});
/*
exports.QrReader = functions.storage.object().onFinalize(async(object) => {
    // File and directory paths.
    const filePath = object.name;
    const contentType = object.contentType; // This is the image MIME type
    const fileDir = path.dirname(filePath);
    const fileName = path.basename(filePath);
    if (!contentType.startsWith('image/')) {
        console.log('This is not an image.');
        return;
    }
    if (!fileName.startsWith('croped_')) {
        console.log('This is not an qr image we test for.');
        return;
    }
    console.log('This is the filePath: ' + filePath);
    //Users/hila.kornis2@gmail.com
    console.log('This is the Users: ' + filePath);
    console.log('This is the fileDir: ' + fileDir);

    //this is the current user:
    // var str = fileDir.substring(6, fileDir.length);
    // var current_user = str.replace(/\.|\$|\[|\]/g, "_");
    // console.log('This is the current user: ' + current_user);



    console.log('This is the fileName: ' + fileName);
    console.log('This is the fileName without : ' + fileName.substring(0, fileName.length - 4));
    const tempLocalFile = path.join(os.tmpdir(), filePath);
    const tempLocalDir = path.dirname(tempLocalFile);
    // // Cloud Storage files.
    const bucket = admin.storage().bucket(object.bucket);
    const file = bucket.file(filePath);
    await mkdirp(tempLocalDir);
    await file.download({ destination: tempLocalFile });
    console.log('The file has been downloaded to', tempLocalFile);
    // const width = 100; //in pixels
    // const height = 100; //in pixels

    const width = 100; //in pixels
    const height = 100; //in pixels

    const Uint8ClampedArray = require('typedarray').Uint8ClampedArray;
    const jpegData = fs.readFileSync(tempLocalFile);
    const rawImageData = jpeg.decode(jpegData);
    let clampedArray = new Uint8ClampedArray(rawImageData.data.length);
    let i;
    for (i = 0; i < rawImageData.data.length; i++) {
        clampedArray[i] = rawImageData.data[i];
    }
    const code = jsqr_1.default(clampedArray, width, height);


    //finding the index:
    let remove_from_end = "captured_1234";
    let last_digit_index = fileName.length - remove_from_end;
    let first_digit_index = fileName.length - remove_from_end - 1;
    console.log('this is the index of the cropped image: ' + fileName.substring(first_digit_index, last_digit_index));

    if (code) {
        console.log("Found QR code", code);

        // var usersRef = ref.child("current_level");
        // var usersRef = ref.child(current_user);
        var original_pic_name = fileName.substring(7, fileName.length - 4)
        console.log('This is the original pic name: ' + original_pic_name);

        //finding the index:
        let remove_from_end = "captured_1234";
        let last_digit_index = fileName.length - remove_from_end;
        let first_digit_index = fileName.length - remove_from_end - 1;
        console.log('this is the index of the cropped image: ' + fileName.substring(first_digit_index, last_digit_index));

        var usersRef = ref.child(original_pic_name);
        usersRef.child(fileName.substring(0, fileName.length - 4)).set({
            fileName: fileName,
            TypeMessage: "QR code is",
            QR_Code: code.data,
            Index: "1" //todo later change
        });



        // const fileName2 = 'test002.txt';
        //
        // const metadata2 = {
        //     contentType: contentType,
        //     metadata: {
        //         firebaseStorageDownloadTokens: uuid
        //     },
        //     // To enable Client-side caching you can set the Cache-Control headers here. Uncomment below.
        //     'Cache-Control': 'public,max-age=3600'
        // };
        //
        // const tempFilePath = path.join(os.tmpdir(), fileName2);
        // console.log({ tempFilePath });
        // // const content = 'id,firstname,lastname\n1,John,Doe\n2,Jane,Doe';
        // const content = 'code is: ' + code;
        // fs.writeFileSync(tempFilePath, content);
        // // const bucket = admin.storage().bucket(Object.bucket);
        // // const bucket2 = await admin.storage().bucket();
        // //
        // // const p = bucket.upload(current_temp_location, { destination: final_pic_location[i], uploadType: "media", metadata: metadata });
        // await bucket.upload(tempFilePath, {            destination: `exports/${fileName2}`,   uploadType: "text", metadata: metadata2      });
        // // Raw string is the default if no format is provided
        // // var message = 'This is my message.';
        // // let message = 'This is my message.';
        // // ref.putString(message).then(function(snapshot) {
        // //     console.log('Uploaded a raw string!');
        // // });


    }
    if (!code) {
        console.log("Not found any");
    }
    // create .txt file called 'final_fileName', which will contain the qr code , or 0 if no code
    fs.unlinkSync(tempLocalFile);
}); */

// Create function which couts the 'finish_...'.txt files, and if count == 48' creates string,
// sends to LAVA, and delete all .txt files