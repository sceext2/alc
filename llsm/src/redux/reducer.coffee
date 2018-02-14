# reducer.coffee, alc/llsm/src/redux/

Immutable = require 'immutable'

state = require './state'
ac = require './action'

_check_init = ($$o) ->
  if ! $$o?
    $$o = Immutable.fromJS state
  $$o

reducer = ($$state, action) ->
  $$o = _check_init $$state
  switch action.type
    when ac.A_SET_IS_RUNNING
      $$o = $$o.set 'is_running', action.payload
    when ac.A_SET_CONFIG_SERVER
      $$o = $$o.setIn ['config', 'server'], action.payload
    when ac.A_SET_CONFIG_SCREEN_SIZE
      $$o = $$o.setIn ['config', 'screen_size'], action.payload
    when ac.A_SET_CONFIG_FPS
      $$o = $$o.setIn ['config', 'fps'], action.payload
  $$o

module.exports = reducer
