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
                    title: message['senderId'],
                    message: message['taskId']
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
    console.log('Croping image and read QR tarted');
    const filePath = object.name;
    const contentType = object.contentType; // This is the image MIME type
    const fileDir = path.dirname(filePath);
    const fileName = path.basename(filePath);

    const tempLocalFile = path.join(os.tmpdir(), filePath);
    const tempLocalDir = path.dirname(tempLocalFile);

    // var basic_qr_size = "375X390";
    // var go_down = 945;
    // var go_left = 88;

    var basic_qr_size = "110X110";
    // var go_down = 242;
    // var go_left = 10;

    var QR_grid = ["24+217", "142+214", "258+214", "377+212", "493+211", "611+207",
        "26+337", "146+335", "261+335", "377+331", "495+331", "614+327",
        "28+453", "146+450", "265+450", "380+450", "496+446", "613+444",
        "30+568", "150+568", "265+566", "380+565", "497+564", "614+564",
        "33+685", "151+684", "264+683", "381+680", "497+681", "615+680",
        "34+921", "150+919", "267+916", "382+917", "504+917", "622+919",
        "34+1036", "153+1037", "270+1037", "388+1036", "506+1037", "623+1036"
    ];


    // Exit if this is triggered on a file that is not an image.
    if (!contentType.startsWith('image/')) {
        return console.log('This is not an image.');
    }

    // Exit if the image is already a croped.
    if (fileName.startsWith('croped_') || !fileName.endsWith('1328.jpg')) {
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
    // var line = [0, 1];
    // var colume = [0, 1, 2, 3, 4, 5];
    var code_values_array = [];
    QR_grid.forEach(QR_location => {

        cropedFilePath = path.normalize(path.join(fileDir, `${croped_PREFIX}${i}_${fileName}`));
        current_location = path.join(os.tmpdir(), cropedFilePath);

        final_pic_location.push(cropedFilePath)
        temp_crop_pic_location.push(current_location);

        // Generate a croped using ImageMagick.
        const p = spawn('convert', [tempLocalFile, '-gravity', 'North-West', '-crop', basic_qr_size + "+" + QR_location, current_location], {
            capture: ['stdout', 'stderr']
        });
        //await spawn('convert', [tempLocalFile, '-strip', '-interlace', 'Plane', '-quality', '0', tempLocalFile]);
        promises.push(p);
        console.log('croped image created at', current_location);
        i++;

    });

    // line.forEach(QR_line => {
    //     colume.forEach(QR_coulume => {
    //         cropedFilePath = path.normalize(path.join(fileDir, `${croped_PREFIX}${i}_${fileName}`));
    //         current_location = path.join(os.tmpdir(), cropedFilePath);

    //         final_pic_location.push(cropedFilePath)
    //         temp_crop_pic_location.push(current_location);

    //         // Generate a croped using ImageMagick.
    //         const p = spawn('convert', [tempLocalFile, '-gravity', 'North-West', '-crop', basic_qr_size + "+" +
    //             (go_left + 120 * QR_coulume) + "+" + (go_down + 120 * QR_line), current_location
    //         ], {
    //             capture: ['stdout', 'stderr']
    //         });
    //         //await spawn('convert', [tempLocalFile, '-strip', '-interlace', 'Plane', '-quality', '0', tempLocalFile]);
    //         promises.push(p);
    //         console.log('croped image created at', current_location);
    //         i++;
    //     });
    // });

    await Promise.all(promises)

    promises = [];
    i = 0;

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
    // await Promise.all(promises)

    // promises = [];
    // i = 0;

    const width = 100; //in pixels
    const height = 100; //in pixels

    const Uint8ClampedArray = require('typedarray').Uint8ClampedArray;

    MAX_ARRAY_LENGTH = 2000000;

    temp_crop_pic_location.forEach(current_temp_location => {

        let base64string = fs.readFileSync(current_temp_location)
        const imageData = Buffer.from(base64string, 'base64');

        var rawImageData = jpeg.decode(imageData);

        var clampedArray = new Uint8ClampedArray(rawImageData.data.length);
        for (var j = 0; j < rawImageData.data.length; j++) {
            clampedArray[j] = rawImageData.data[j];
        }


        console.log("Tring to read QR code in block " + i);
        const code = jsQR(clampedArray, rawImageData.width, rawImageData.height);

        if (code) {
            console.log("Found QR code in block " + i + " ", code.data);
            code_values_array.push(code.data);
        } else {
            console.log("Couldn't find QR code in block " + i);
            code_values_array.push("NaN");
        }
        i++;
    })

    i = 0;

    // upload pics from temp location to directory
    temp_crop_pic_location.forEach(current_temp_location => {
        if (code_values_array[i] === "NaN") {
            const p = bucket.upload(current_temp_location, { destination: final_pic_location[i], uploadType: "media", metadata: metadata });
            promises.push(p)
            console.log('croped uploaded to Storage at', final_pic_location[i]);
        }
        i++;
    })

    // temp_crop_pic_location.forEach(current_temp_location => {
    //     const p = bucket.upload(current_temp_location, { destination: final_pic_location[i], uploadType: "media", metadata: metadata });
    //     promises.push(p)
    //     console.log('croped uploaded to Storage at', final_pic_location[i]);
    //     i++;

    // })

    // Wait for all croped pics to be uploaded
    await Promise.all(promises)

    // Once the image has been uploaded delete the local files to free up disk space.
    temp_crop_pic_location.forEach(current_temp_location => {
        fs.unlinkSync(current_temp_location);
    })
    fs.unlinkSync(tempLocalFile);

    var original_pic_name = fileName.substring(0, fileName.length - 13)
    var senderName = original_pic_name.substring(0, original_pic_name.length - 18)
    var topic_name = original_pic_name.substring(original_pic_name.length - 17, original_pic_name.length)

    console.log('Original pic name is : ' + original_pic_name);
    console.log('User name is : ' + senderName);
    console.log('Topic name is : ' + topic_name);

    var return_QR_values_string = '';

    code_values_array.forEach(current_code => {
        return_QR_values_string += current_code;
        return_QR_values_string += ',';
    });

    return_QR_values_string = return_QR_values_string.substring(0, return_QR_values_string.length - 1);

    console.log('The return value is: ' + return_QR_values_string);

    var myMessage = {
        data: {
            title: "capture request result",
            message: return_QR_values_string,
        },
        topic: topic_name
    };

    console.log('Function done');

    return admin.messaging().send(myMessage)
        .then((response) => {
            // Response is a message ID string.
            // Here we can add a log to see how the sending went
            console.log('Successfully sent result message:', response);
            // userDoc = admin.firestore().doc('Users/' + senderName).get();
            return admin.firestore().doc("users/" + senderName).get();
        });
});