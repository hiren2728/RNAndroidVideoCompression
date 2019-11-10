/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React,{Component} from 'react';
import {
    SafeAreaView,
    StyleSheet,
    View,
    Text,
    StatusBar,
    TouchableOpacity,
    NativeModules
} from 'react-native';


import RNFS from 'react-native-fs';
import ImagePicker from 'react-native-image-crop-picker';

class App extends Component {

    getFileInfo = async (fileURL) => {
        RNFS.stat(fileURL).then(result => {

        }).catch(e => {
            console.log('Error ::',e.message)
        })
    };

    btnOpenSettingClick = () => {
        NativeModules.CompressVideo.openNetworkSettings((result) => {
            console.log('result ::',result)
        })
    };

    btnPickVideoClick = () => {
        ImagePicker.openPicker({
            mediaType: "video",
        }).then((video) => {
            console.log(video);
            let path = video.path.replace("file://","");

            // var destination = RNFS.DocumentDirectoryPath + '/SampleVideo_1280x720_10mb.mp4';
            // RNFS.copyFile(path,destination).then(() => {
            //     console.log('Success ::',destination)
            // }).catch(e => console.log('E ::',e));

            NativeModules.CompressVideo.compressVideo(path,result => {
                console.log('Result ::',result);
                let destination = result.replace('file:');
                RNFS.stat(`file://${destination}`).then(result => {
                    console.log('Result ::',result)
                }).catch(e => {
                    console.log('Error ::',e.message)
                })
            })
        });
    };

    render(): * {
        return(
            <>
                <StatusBar barStyle="dark-content"/>
                <SafeAreaView style={{flex : 1}}>
                    <View style={{flex : 1,backgroundColor : 'gray',justifyContent : 'center',alignItems : 'center'}}>

                        <TouchableOpacity
                            onPress={this.btnPickVideoClick}
                            style={{width : 150,height : 40,backgroundColor : 'blue',justifyContent : 'center',alignItems : 'center',marginBottom : 20}}>
                            <Text style={{color : 'white'}}>
                                {'Open Gallery'}
                            </Text>
                        </TouchableOpacity>


                        <TouchableOpacity
                            onPress={this.btnOpenSettingClick}
                            style={{width : 150,height : 40,backgroundColor : 'blue',justifyContent : 'center',alignItems : 'center'}}>
                            <Text style={{color : 'white'}}>
                                {'Open Settings'}
                            </Text>
                        </TouchableOpacity>
                    </View>
                </SafeAreaView>
            </>
        )
    }
}

export default App;
