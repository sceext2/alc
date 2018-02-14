# index.coffee, alc/llsm/src/

{ AppRegistry } = require 'react-native'

Main = require './main'

AppRegistry.registerComponent 'llsm_main', () ->
  Main

module.exports = Main
