AC_PREREQ([2.0])
AC_INIT([DPDKLIB], [0.01], [ash112@imperial.ac.uk])

AC_CONFIG_MACRO_DIR([m4])
AM_INIT_AUTOMAKE([-Wall -Werror foreign])

AC_CONFIG_FILES([Makefile])
AC_PROG_CC
AC_PROG_LD
AM_PROG_AR

LT_INIT
link_all_deplibs=yes
link_all_deplibs_CC=yes
LIBTOOL="$LIBTOOL --preserve-dup-deps"
AC_SUBST(LIBTOOL)dnl
AC_OUTPUT
