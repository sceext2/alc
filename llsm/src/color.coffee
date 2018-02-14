# color.coffee, alc/llsm/src/

# main color
_MC_BLACK = 'hsl(0, 0%, 0%)'
_MC_WHITE = 'hsl(0, 0%, 100%)'
_MC_GRAY = 'hsl(0, 0%, 50%)'

# BG color
BG = _MC_BLACK
BG_SEC = _MC_GRAY

# button
BG_BTN = _MC_GRAY
# TODO danger button

BG_TOUCH = _MC_WHITE

# border
BORDER = _MC_GRAY

# text
TEXT = _MC_WHITE
TEXT_SEC = _MC_GRAY
TEXT_BG = _MC_BLACK


module.exports = {
  BG
  BG_SEC

  BG_BTN
  BG_TOUCH

  BORDER

  TEXT
  TEXT_SEC
  TEXT_BG
}
