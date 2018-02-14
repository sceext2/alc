# index.coffee, alc/llsm/src/

{
  createStore
  applyMiddleware
} = require 'redux'
{ default: thunk } = require 'redux-thunk'

{ Provider } = require 'react-redux'

{ createElement: cE } = require 'react'
cC = require 'create-react-class'
PropTypes = require 'prop-types'


reducer = require './redux/reducer'

Main = require './main'

# redux store
store = createStore reducer, applyMiddleware(thunk)

O = cC {
  render: ->
    (cE Provider, {
      store
      },
      (cE Main)
    )
}


{ AppRegistry } = require 'react-native'

AppRegistry.registerComponent 'llsm_main', () ->
  O

module.exports = {
  store
  O
}
