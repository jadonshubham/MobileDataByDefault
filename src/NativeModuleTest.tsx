import {View, Text, Pressable, StyleSheet, ToastAndroid} from 'react-native';
import React from 'react';
import NetworkModule from './NetworkModule';

const NativeModuleTest = () => {
  const changeNetworkToCellular = async () => {
    try {
      await NetworkModule.changeNetworkToCellular();
    } catch (err) {
      console.log(err);
    }
  };

  const changeNetworkToDefault = () => {
    NetworkModule.changeNetworkToWifi();
  };

  const downloadFile = () => {
    fetch(
      'https://freetestdata.com/wp-content/uploads/2022/11/Free_Test_Data_10.5MB_PDF.pdf',
    )
      .then(() => {
        console.log('File Downloded');
        ToastAndroid.show('FileDownloaded', 2000);
      })
      .catch(() => {
        console.log('File Downloded Error');
        ToastAndroid.show('File Download Error', 2000);
      });
  };

  return (
    <View style={styles.container}>
      <View style={styles.row}>
        <Pressable
          onPress={changeNetworkToCellular}
          style={[styles.buttonContainer, styles.greenButton]}>
          <Text style={styles.buttonText}>Restrict to Mobile Data</Text>
        </Pressable>
        <Pressable
          onPress={changeNetworkToDefault}
          style={styles.buttonContainer}>
          <Text style={styles.buttonText}>Allow both</Text>
        </Pressable>
      </View>
      <Pressable
        onPress={downloadFile}
        style={[styles.buttonContainer, styles.downloadButton]}>
        <Text style={styles.buttonText}>Download File</Text>
      </Pressable>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  row: {
    flexDirection: 'row',
    marginBottom: 12,
  },
  buttonContainer: {
    backgroundColor: 'black',
    padding: 12,
    flex: 1,
    alignItems: 'center',
  },
  greenButton: {
    backgroundColor: 'green',
  },
  buttonText: {
    color: '#fff',
  },
  downloadButton: {
    flex: 0,
    backgroundColor: 'orange',
  },
});
export default NativeModuleTest;
