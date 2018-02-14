# texts.coffee, alc/llsm/src/sub/

{ createElement: cE } = require 'react'
cC = require 'create-react-class'
PropTypes = require 'prop-types'

{
  View
  Text
} = require 'react-native'

co = require '../color'
ss = require '../style'


TextS = cC {
  displayName: 'TextS'
  propTypes: {
    text: PropTypes.string.isRequired
    placeholder: PropTypes.string.isRequired
  }

  render: ->
    text = @props.text
    if text is ''
      text = @props.placeholder

    (cE View, {
      style: {
        margin: ss.TOP_PADDING / 2
        marginLeft: 0
        marginRight: 0

        # no border, but add border width
        padding: ss.TOP_PADDING / 2 + ss.BORDER_WIDTH * 1.5
        paddingTop: ss.BORDER_WIDTH * 1.5
        paddingBottom: ss.BORDER_WIDTH * 1.5
        #borderWidth: ss.BORDER_WIDTH * 1.5
      } },
      (cE Text, {
        style: {
          fontSize: ss.TEXT_SIZE
          color: co.TEXT
          fontFamily: 'monospace'

          backgroundColor: co.BG
          #flexWrap: 'wrap'  # FIXME
        } },
        text
      )
    )
}

module.exports = TextS
