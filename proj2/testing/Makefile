# This makefile is defined to give you the following targets:
#
#    default: Same as check
#    check: For each file, F.in, use F.in as input to "java $(MAIN_CLASS)" and
#          compare the output to the contents of the file names F.out.
#          Report discrepencies.
#    clean: Remove all the .class files produced by java compilation, 
#          all Emacs backup files, and testing output files.
#
# You can use this file without understanding most of it, of course, but
# I strongly recommend that you try to figure it out, and where you cannot,
# that you ask questions.  The Lab Reader contains documentation.

SHELL = /bin/bash

# Flags to Java interpreter: check assertions
JFLAGS = -ea

# See comment in db61b/Makefile.
CPATH = "..:$(CLASSPATH):;..;$(CLASSPATH)"

TEST_LOA = test-loa

TESTER_FLAGS =

TESTS := *.in

.PHONY: default check clean std

# First, and therefore default, target.
default:
	$(MAKE) -C .. 
	$(MAKE) check

# The "CLASSPATH=..." clause in front of the command below temporarily 
# changes the location in which Java searches for classes to be the directory
# that contains your project.
check: $(CLASSES)
	@echo "Testing application loa.Main..."
	@CLASSPATH=$(CPATH) $(TEST_LOA) $(TESTER_FLAGS) $(TESTS)

# 'make clean' will clean up stuff you can reconstruct.
clean:
	$(RM) -r *~ OUTPUT* *.log *.diff __pycache__
