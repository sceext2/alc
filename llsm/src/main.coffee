# main.coffee, alc/llsm/src/

{ createElement: cE } = require 'react'
cC = require 'create-react-class'
PropTypes = require 'prop-types'

{
  View
  Text
} = require 'react-native'

co = require './color'
ss = require './style'

Input = require './sub/input'
Button = require './sub/button'
TextS = require './sub/texts'


Main = cC {
  propTypes: {
    is_running: PropTypes.bool.isRequired

    config_server: PropTypes.string.isRequired
    config_screen_size: PropTypes.string.isRequired
    config_fps: PropTypes.string.isRequired

    on_change_server: PropTypes.func.isRequired
    on_change_size: PropTypes.func.isRequired
    on_change_fps: PropTypes.func.isRequired

    on_start: PropTypes.func.isRequired
  }

  _input_or_texts: (text, placeholder, on_change) ->
    if @props.is_running
      (cE TextS, {
        text
        placeholder
        })
    else
      (cE Input, {
        text
        placeholder
        on_change
        })

  render: ->
    (cE View, {
      style: {
        flex: 1
        flexDirection: 'column'
        backgroundColor: co.BG
      } },
      # title
      (cE View, {
        style: {
          padding: ss.TOP_PADDING
          paddingTop: 0
          flexDirection: 'row'
          justifyContent: 'center'
          alignItems: 'center'
          # border
          borderBottomWidth: ss.BORDER_WIDTH
          borderColor: co.BORDER
        } },
        (cE Text, {
          style: {
            fontSize: ss.TITLE_SIZE
            color: co.TEXT_SEC
          } },
          'LLSM'
        )
      )
      # main config
      (cE View, {
        style: {
          flex: 1
          padding: ss.TOP_PADDING
        } },
        # server
        (cE View, {
          style: {
            # TODO
          } },
          (cE Text, {
            style: {
              fontSize: ss.TITLE_SIZE
              color: co.TEXT_SEC
            } },
            'Server'
          )
          @_input_or_texts(@props.config_server, 'IP:port', @props.on_change_server)
        )
        # video
        (cE View, {
          style: {
            marginTop: ss.TOP_PADDING
          } },
          (cE Text, {
            style: {
              fontSize: ss.TITLE_SIZE
              color: co.TEXT_SEC
            } },
            'Video'
          )
          (cE View, {
            style: {
              flexDirection: 'row'
              justifyContent: 'center'
              alignItems: 'center'
            } },
            # px
            (cE View, {
              style: {
                flex: 2
              } },
              @_input_or_texts(@props.config_screen_size, 'X x Y', @props.on_change_size)
            )
            (cE Text, {
              style: {
                fontSize: ss.TITLE_SIZE
                padding: ss.TOP_PADDING
                color: co.TEXT_SEC
              } },
              'px @'
            )
            # fps
            (cE View, {
              style: {
                flex: 1
              } },
              @_input_or_texts(@props.config_fps, 'N', @props.on_change_fps)
            )
            (cE Text, {
              style: {
                fontSize: ss.TITLE_SIZE
                paddingLeft: ss.TOP_PADDING
                color: co.TEXT_SEC
              } },
              'fps'
            )
          )
        )
      )
      # main button
      (cE View, {
        style: {
          marginTop: ss.TOP_PADDING
          # border
          borderTopWidth: ss.BORDER_WIDTH
          borderColor: co.BORDER
        } },
        @_render_button()
      )
    )

  # TODO support stop function
  _render_button: ->
    if ! @props.is_running
      (cE Button, {
        text: 'start'
        on_press: @props.on_start
        })
}


# connect for redux
{ connect } = require 'react-redux'

action = require './redux/action'
op = require './redux/op'

mapStateToProps = ($$state, props) ->
  {
    is_running: $$state.get 'is_running'

    config_server: $$state.getIn ['config', 'server']
    config_screen_size: $$state.getIn ['config', 'screen_size']
    config_fps: $$state.getIn ['config', 'fps']
  }

mapDispatchToProps = (dispatch, props) ->
  o = Object.assign {}, props

  o.on_change_server = (text) ->
    dispatch action.set_config_server(text)
  o.on_change_size = (text) ->
    dispatch action.set_config_screen_size(text)
  o.on_change_fps = (text) ->
    dispatch action.set_config_fps(text)

  o.on_start = ->
    dispatch op.start_sm()

  o

module.exports = connect(mapStateToProps, mapDispatchToProps)(Main)
