CFLAGS := -std=c17 -Wall -Wextra -Werror -Wno-unused-parameter

ifeq ($(MODE),debug)
	CFLAGS += -O0 -DEBUG -g
	BUILD_DIR := build/debug
else
	CFLAGS := -O3 -flto
	BUILD_DIR := build/release
endif

HEADERS := $(wildcard $(SOURCE_DIR)/*.h)
SOURCES := $(wildcard $(SOURCE_DIR)/*.c)
OBJECTS := $(addprefix $(BUILD_DIR)/$(NAME)/, $(notdir $(SOURCES:.c=.o)))

# Targets

build/$(NAME): $(OBJECTS)
	@ mkdir -p build
	@ $(CC) $(CFLAGS) $^ -o $@

$(BUILD_DIR)/$(NAME)/%.o: $(SOURCE_DIR)/%.c $(HEADERS)
	@ mkdir -p $(BUILD_DIR)/$(NAME)
	@ $(CC) -c $(C_LANG) $(CFLAGS) -o $@ $<

.PHONY: default
