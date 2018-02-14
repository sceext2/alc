# input.coffee, alc/llsm/src/sub/

{ createElement: cE } = require 'react'
cC = require 'create-react-class'
PropTypes = require 'prop-types'

{
  View
  TextInput
} = require 'react-native'

co = require '../color'
ss = require '../style'


Input = cC {
  displayName: 'Input'
  propTypes: {
    text: PropTypes.string.isRequired
    placeholder: PropTypes.string.isRequired

    on_change: PropTypes.func.isRequired
  }

  render: ->
    (cE View, {
      style: {
        margin: ss.TOP_PADDING / 2
        marginLeft: 0
        marginRight: 0
        padding: ss.TOP_PADDING / 2
        paddingTop: 0
        paddingBottom: 0
        # border
        borderWidth: ss.BORDER_WIDTH * 1.5
        borderColor: co.TEXT_SEC
        borderRadius: ss.TOP_PADDING / 2
      } },
      (cE TextInput, {
        value: @props.text
        placeholder: @props.placeholder

        autoCapitalize: 'none'
        autoCorrect: false
        #autoGrow: false  # FIXME
        underlineColorAndroid: 'transparent'

        onChangeText: @props.on_change

        style: {
          fontSize: ss.TEXT_SIZE
          color: co.TEXT
          fontFamily: 'monospace'

          backgroundColor: co.BG
          #flexWrap: 'wrap'  # FIXME
        } })
    )
}

module.exports = Input
