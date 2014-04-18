import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.util.Scanner;

import static java.lang.System.out;

public class ShowGenericType
{
	public static StringBuffer output = new StringBuffer();
	public static StringBuffer outputImport = new StringBuffer();
	public static String pkg;
	public static String input;
	public static String space = "	";
	public static String spaceClass = "";

	public static void main(String[] args)
	{
		for (;;)
		{
			if (WelcomeInput())
			{
				ProcessGenericClass(input);
				printResult();
			}
		}
	}

	public static Boolean WelcomeInput()
	{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		out.println("[Apjp 2013 HW1_GenericClass by 100306039]");
		out.println(">Please enter a class's canonical name ( e.g. java.util.List) :");
		input = String.valueOf(scanner.next());
		try
		{
			Class.forName(input);
			out.printf(">Hello! Your input is '%s' \n\n\n", input);

			return true;
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			out.println("Your input should be the canonical name of a class. Please try again!");
			out.println("\n=========================================");
			return false;
		}
	}

	public static void ProcessGenericClass(String input)
	{
		try
		{
			Class<?> myClass = Class.forName(input);
			mainClass(myClass, input);
			innerClass(myClass, input);

		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void mainClass(Class<?> myClass, String input)
	{

		ProcessGeneric(myClass, "");
		pkg = myClass.getPackage().getName();

	}

	public static void innerClass(Class<?> myClass, String input)
	{

		Class<?>[] innerClass = myClass.getDeclaredClasses();
		space = "		";
		spaceClass = "	";
		output.append("\n");
		int count = 0;
		for (Class<?> ic : innerClass)
		{
			if (ic != null && ic.getCanonicalName() != null
					&& ic.getDeclaredMethods().length > 0)
			{
				if (count == 0)
					ProcessGeneric(ic, "innerfirst");
				else
					ProcessGeneric(ic, "inner");
				count++;
			}

		}

	}

	public static void printResult()
	{
		out.println("package " + pkg + ";");
		out.println(outputImport);
		out.println(output);
		out.println("}");
		out.println("\n=========================================");
	}

	public static void ProcessGeneric(Class<?> myClass, String inner)
	{
		Class<?> mySuperClass = myClass.getSuperclass();
		Class<?>[] myInterface = myClass.getInterfaces();
		Constructor<?>[] myConstructor = myClass.getConstructors();
		Field myField[] = myClass.getDeclaredFields();
		Method myMethod[] = myClass.getDeclaredMethods();

		if (inner == "inner" || inner == "innerfirst")
		{
			if (inner == "innerfirst")
				output.append(spaceClass);
			output.append(Class(myClass));
			output.append(SuperClass(mySuperClass));
			output.append(Interface(myInterface));
			output.append(" {\n");
			output.append(Constructor(myConstructor));
			// output.append(Field(myField));
			output.append(Method(myMethod));
			if (inner == "inner" || inner == "innerfirst")
				output.append("\n" + spaceClass + "}");
		} else
		{
			output.append(Class(myClass));
			output.append(SuperClass(mySuperClass));
			output.append(Interface(myInterface));
			output.append(" {\n");
			output.append(Constructor(myConstructor));
			output.append(Field(myField));
			output.append(Method(myMethod));
		}

	}

	public static String TypeVariable(TypeVariable<?>[] myTypeVariable)
	{
		/* TypeVariable */
		int TypeVariable = 0;
		String tv = "";
		for (TypeVariable<?> t : myTypeVariable)
		{
			TypeVariable++;
			if (TypeVariable == 1)
			{
				tv += (t.getName());
			} else
			{
				tv += ("," + t.getName());
			}

		}
		if (TypeVariable != 0)
			return ("<" + tv + ">");
		else
		{
			return "";
		}
	}

	public static String Class(Class<?> myClass)
	{
		TypeVariable<?>[] myTypeVariable = myClass.getTypeParameters();
		return ((Modifier.toString(myClass.getModifiers()) + " class " + myClass
				.getSimpleName()) + TypeVariable(myTypeVariable));
	}

	public static String ClassWithoutModifier(Class<?> myClass)
	{

		TypeVariable<?>[] myTypeVariable = myClass.getTypeParameters();
		return (myClass.getSimpleName() + TypeVariable(myTypeVariable));
	}

	public static String SuperClass(Class<?> mySuperClass)
	{
		if (mySuperClass != null)
		{

			String superClassName = mySuperClass.getCanonicalName();
			if (superClassName != null && superClassName.contains("java."))
			{
				Import(superClassName);
				superClassName = superClassName.substring(superClassName
						.lastIndexOf(".") + 1);

			}

			TypeVariable<?>[] mySuperClassTypeVariable = mySuperClass
					.getTypeParameters();
			return (" extends " + superClassName + TypeVariable(mySuperClassTypeVariable));
		}
		return "";
	}

	public static String Interface(Class<?>[] myInterface)
	{
		/* Interface */
		int Interface = 0;
		String infItem = "";
		for (Class<?> inf : myInterface)
		{
			Interface++;
			String infName = inf.getCanonicalName();
			if (infName != null && infName.contains("java."))
			{
				Import(infName);
				infName = infName.substring(infName.lastIndexOf(".") + 1);
			}
			TypeVariable<?>[] myInfTypeVariable = inf.getTypeParameters();
			if (Interface == 1)
			{
				infItem += (" implements " + infName + TypeVariable(myInfTypeVariable));
			} else
			{
				infItem += (", " + infName);
			}

		}
		return infItem;
	}

	public static String Constructor(Constructor<?>[] myConstructor)
	{
		/* Constructor */
		String construtorItem = "";
		int cons = 0;
		for (Constructor<?> c : myConstructor)
		{
			cons++;
			if ((Modifier.isPublic(c.getModifiers())))
			{
				String construtor = c.toGenericString();
				construtor = ImportForConstructor(construtor);

				construtorItem += (space + construtor + " " + "{\n" + "	" + "}\n");
			}
		}
		if (cons > 0)
		{
			return construtorItem;
		}
		return "";
	}

	public static String Field(Field[] myField)
	{
		String fieldItem = "";
		for (Field f : myField)
		{
			String field = f.toGenericString();
			if (field != null && field.contains("java."))
			{
				field = Import(field);

			}
			fieldItem += (space + field + " " + ";\n");
		}
		return fieldItem;
	}

	public static String Method(Method[] myMethod)
	{
		String methodItem = "";
		for (Method m : myMethod)
		{
			String method = m.toGenericString();
			if (method != null && method.contains("java."))
			{

				if (method.contains("throws"))
				{
					String m1 = method.substring(0, method.indexOf("throws"));
					String m2 = method.substring(method.indexOf("throws"));
					m1 = ImportForMethod(m1);
					m2 = Import(m2);
					method = m1 + m2;
				} else
				{
					method = ImportForMethod(method);
				}

			}

			methodItem += (space + method + " " + ";\n");
		}
		return methodItem;
	}

	public static String Import(String name)
	{
		if (name != null && name.contains("java."))
		{
			String importItem = "";

			while (name.contains("java."))
			{
				int startpoint = name.lastIndexOf("java.");
				int endpoint = -1;
				if (name.indexOf("[", startpoint) != -1)
				{
					endpoint = name.indexOf("[", startpoint);
				} else if (name.indexOf(" ", startpoint) != -1)
				{
					endpoint = name.indexOf(" ", startpoint);
				} else
				{
					endpoint = name.length();
				}

				importItem = name.substring(startpoint, endpoint);

				name = name.replaceFirst(importItem.substring(0,
						importItem.lastIndexOf(".") + 1), "");
				name = name.replace("$", ".");

			}

			if (!importItem.contains("lang") && !importItem.contains(input)
					&& outputImport.indexOf(importItem) < 0)
			{
				outputImport.append("import " + importItem + ";\n");
			}
		}
		return name;
	}

	public static String ImportForConstructor(String name)
	{
		if (name != null && name.contains("java."))
		{
			String importItem = "";

			while (name.contains("java."))
			{
				int startpoint = name.lastIndexOf("java.");
				int endpoint = -1;
				if (name.indexOf("<", startpoint) != -1)
				{
					endpoint = name.indexOf("<", startpoint);
					String substr = name.substring(startpoint, endpoint);
					endpoint = substr.lastIndexOf(".") + startpoint;
				} else if (name.indexOf("(", startpoint) != -1)
				{
					endpoint = name.indexOf("(", startpoint);
					String substr = name.substring(startpoint, endpoint);
					endpoint = substr.lastIndexOf(".") + startpoint;
				} else
				{
					endpoint = name.indexOf(")", startpoint);
				}
				if (endpoint == -1)
				{
					break;
				}
				importItem = name.substring(startpoint, endpoint);

				String tempname = name.replace(importItem + ".", "");
				name = tempname.replaceFirst(importItem.substring(0,
						importItem.lastIndexOf(".") + 1), "");

			}

			if (!importItem.contains("lang") && !importItem.contains(input)
					&& outputImport.indexOf(importItem) < 0)
			{
				outputImport.append("import " + importItem + ";\n");
			}
		}
		return name;
	}

	public static String ImportForMethod(String name)
	{
		if (name != null && name.contains("java."))
		{
			String importItem = "";

			while (name.contains("java."))
			{
				int startpoint = name.lastIndexOf("java.");
				int endpoint = -1;
				if (name.indexOf("[", startpoint) != -1)
				{
					endpoint = name.indexOf("[", startpoint);
				} else if (name.indexOf("<", startpoint) != -1)
				{
					endpoint = name.indexOf("<", startpoint);
				} else if (name.indexOf("(", startpoint) != -1)
				{
					endpoint = name.indexOf("(", startpoint);
				} else
				{
					endpoint = name.indexOf(")", startpoint);
				}
				if (endpoint == -1)
				{
					break;
				}
				importItem = name.substring(startpoint, endpoint);

				String tempname = name.replace(input + ".", "");
				tempname = tempname.replace(importItem + ".", "");
				name = tempname.replaceFirst(importItem.substring(0,
						importItem.lastIndexOf(".") + 1), "");
				name = name.replace("$", ".");

			}

			if (!importItem.contains("lang") && !importItem.contains(input)
					&& outputImport.indexOf(importItem) < 0)
			{
				outputImport.append("import " + importItem + ";\n");
			}
		}
		return name;
	}
}
