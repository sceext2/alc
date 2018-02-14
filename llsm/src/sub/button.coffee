# button.coffee, alc/llsm/src/sub/

{ createElement: cE } = require 'react'
cC = require 'create-react-class'
PropTypes = require 'prop-types'

{
  View
  Text
  TouchableNativeFeedback
} = require 'react-native'

co = require '../color'
ss = require '../style'


Button = cC {
  displayName: 'Button'
  propTypes: {
    text: PropTypes.string.isRequired

    on_press: PropTypes.func.isRequired
  }

  render: ->
    (cE View, {
      style: {
        margin: ss.TOP_PADDING
      } },
      (cE TouchableNativeFeedback, {
        onPress: @props.on_press
        background: TouchableNativeFeedback.Ripple co.BG_TOUCH
        },
        (cE View, {
          style: {
            height: ss.TOP_HEIGHT
            flexDirection: 'row'
            alignItems: 'center'
            justifyContent: 'center'
            # button border
            borderWidth: ss.BORDER_WIDTH
            borderColor: co.BORDER
            borderRadius: ss.TOP_PADDING / 2
          } },
          (cE Text, {
            style: {
              fontSize: ss.TITLE_SIZE
              color: co.TEXT
            } },
            @props.text
          )
        )
      )
    )
}

module.exports = Button
