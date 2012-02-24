/**
 * This is class is used to combine file vertically,
 * also check the identity between files.
 * 
 * @author wavefancy@gmail.com
 * Date: 2011-11-12
 */
 
 --------------------------------
    verticalCombineFiles    version: 1.0     Author:wavefancy@gmail.com
--------------------------------
Usages: 
parameter1: How many column do you want to skipped?
parameter2: 1,2|1 indentical columns between files, program will check the indentity of these columns between files, and only combine and output the indentical lines.
parameter3: input file 1.
parameter4: input file 2.
parameter5: output file prefix.
Column index start from 1.
--------------------------------

EXAMPLE
--------------------------------

java -jar verticalCombineFiles.jar 2 1,2 file1.txt file2.txt com
# Skip the first 2 columns, that only copy the first 2 columns of the first file to the output file.
# 1,2 : compare the identity of the first two columns between the two input file, and only output the indentical lines.

INPUT FILE1
--------------------------------
CHR	RSID	NA12	NA23
1	rs12	AA	TT
2	rs23	CC	GG

INPUT FILE2
--------------------------------
CHR	RSID	NA12	NA23
1	rs123	AA	TT
2	rs23	CC	GG

OUTPUT FILE: com_combined.txt
--------------------------------
CHR	RSID	NA12	NA23	NA12	NA23
2	rs23	CC	GG	CC	GG


