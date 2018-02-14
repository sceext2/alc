# sm_native.coffee, alc/llsm/src/

{ NativeModules } = require 'react-native'

_n = NativeModules.sm_native


start_sm = (config) ->
  await _n.start_sm JSON.stringify(config)

module.exports = {
  start_sm  # async
}
