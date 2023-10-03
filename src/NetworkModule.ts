import {NativeModules} from 'react-native';

const {NetworkModule} = NativeModules;

interface NetworkInterface {
  changeNetworkToCellular: () => Promise<string>;
  changeNetworkToWifi: () => void;
}

export default NetworkModule as NetworkInterface;
