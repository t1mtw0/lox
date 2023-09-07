BUILD_DIR := build

# Targets

default: clox

clox:
	@ $(MAKE) -f util/c.make NAME=clox MODE=debug SOURCE_DIR=clox

clean:
	@ rm -rf $(BUILD_DIR)

.PHONY: clox default clean
