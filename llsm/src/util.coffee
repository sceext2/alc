# util.coffee, alc/llsm/src/redux/

{ ToastAndroid } = require 'react-native'


toast = (text) ->
  ToastAndroid.show text, ToastAndroid.SHORT

module.exports = {
  toast
}
