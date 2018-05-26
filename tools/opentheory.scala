// DESCRIPTION: Isabelle wrapper for OpenTheory

object Open_Theory extends isabelle.Isabelle_Tool.Body {

  import isabelle._

  lazy val options = Options.init()

  val session = "Open_Theory"

  def apply(args: List[String]): Unit = {
    val results = Build.build(
      options = options,
      build_heap = true,
      sessions = List(session),
      progress = new Console_Progress()
    )

    if (!results.ok)
      error("Build failed")

    val args_ml = ML_Syntax.print_list(ML_Syntax.print_string_bytes)(args)

    val opentheory_ml = s"open_theory $args_ml"

    val proc = ML_Process(
      options = options,
      logic = session,
      args = List("--eval", opentheory_ml)
    )

    val res = proc.result(
      progress_stdout = println _,
      progress_stderr = Console.err.println _
    )

    scala.sys.exit(res.rc)
  }

}
