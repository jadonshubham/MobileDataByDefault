import React, {useEffect} from 'react';
import {View, StyleSheet} from 'react-native';
import NetInfo from '@react-native-community/netinfo';
import NativeModuleTest from './src/NativeModuleTest';

export default function App() {
  useEffect(() => {
    const unsubscribe = NetInfo.addEventListener(state => {
      console.log('Connection type', state.type);
      console.log('Is connected?', state.isConnected);
      console.log('is Internet reachable?', state.isInternetReachable);
      console.log('Details: ', state.details);
    });

    return () => unsubscribe();
  }, []);

  return (
    <View style={styles.container}>
      <NativeModuleTest />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 24,
    backgroundColor: '#fff',
  },
});
