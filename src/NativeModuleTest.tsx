import {View, Text, Pressable, NativeModules, StyleSheet} from 'react-native';
import React from 'react';
import WebView from 'react-native-webview';

const {NetworkModule} = NativeModules;

const NativeModuleTest = () => {
  const onPress = () => {
    NetworkModule.changeNetworkToMobileData();
  };

  const callApi = () => {
    NetworkModule.changeNetworkToWifi();
  };

  return (
    <View style={styles.container}>
      <View style={styles.row}>
        <Pressable
          onPress={onPress}
          style={[styles.buttonContainer, styles.greenButton]}>
          <Text style={styles.buttonText}>Restrict to Mobile Data</Text>
        </Pressable>
        <Pressable onPress={callApi} style={styles.buttonContainer}>
          <Text style={styles.buttonText}>Allow both</Text>
        </Pressable>
      </View>
      <WebView
        source={{uri: 'https://reactnative.dev/'}}
        style={styles.container}
        startInLoadingState
        renderLoading={() => (
          <View>
            <Text>LOADING</Text>
          </View>
        )}
      />
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
});
export default NativeModuleTest;
