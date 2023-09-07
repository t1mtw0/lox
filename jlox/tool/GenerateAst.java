package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args)  throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
            "Assign: Token name, Expr value",
            "Binary: Expr left, Token operator, Expr right",
            "Grouping: Expr expression",
            "Literal: Object value",
            "Logical: Expr left, Token operator, Expr right",
            "Unary: Token operator, Expr right",
            "Variable: Token name",
            "Call: Expr callee, Token paren, List<Expr> arguments",
            "Get: Expr object, Token name",
            "Set: Expr object, Token name, Expr value",
            "This: Token keyword",
            "Super: Token keyword, Token method"
        ));

        defineAst(outputDir, "Stmt", Arrays.asList(
            "Block: List<Stmt> statements",
            "Class: Token name, Expr.Variable superclass, List<Stmt.Function> methods",
            "Expression: Expr expression",
            "Function: Token name, List<Token> params, List<Stmt> body",
            "If: Expr condition, Stmt thenBranch, Stmt elseBranch",
            "Print: Expr expression",
            "Return: Token keyword, Expr value",
            "Var: Token name, Expr initializer",
            "While: Expr condition, Stmt body"
        ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        for (String type: types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("\tabstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("\tstatic class " + className + " extends " + baseName + " {");
        writer.println("\t\t" + className + "(" + fieldList + ") {");

        String[] fields = fieldList.split(", ");
        for (String field: fields) {
            String name = field.split(" ")[1];
            writer.println("\t\t\tthis." + name + " = " + name + ";");
        }

        writer.println("\t\t}");

        writer.println();
        writer.println("\t\t@Override");
        writer.println("\t\t<R> R accept(Visitor<R> visitor) {");
        writer.println("\t\t\treturn visitor.visit" + className + baseName + "(this);");
        writer.println("\t\t}");

        writer.println();
        for (String field: fields) {
            writer.println("\t\tfinal " + field + ";");
        }

        writer.println("\t}");
        writer.println();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("\tinterface Visitor<R> {");

        for (String type: types) {
            String typeName = type.split(":")[0].trim();
            writer.println("\t\tR visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("\t}");
        writer.println();
    }
}
