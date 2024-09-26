package tosbik.ao.parayonetimi.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import tosbik.ao.parayonetimi.R

object AppFontFamily {
    val jetbrainsMono = FontFamily(
        Font(R.font.jb, FontWeight.Bold),
        Font(R.font.jel, FontWeight.ExtraLight),
        Font(R.font.jex, FontWeight.ExtraBold),
        Font(R.font.jl, FontWeight.Light),
        Font(R.font.jm, FontWeight.Medium),
        Font(R.font.jr, FontWeight.Normal),
        Font(R.font.jsm, FontWeight.SemiBold),
        Font(R.font.jt, FontWeight.Thin),
    )

    val suse = FontFamily(
        Font(R.font.sb, FontWeight.Bold),
        Font(R.font.seb, FontWeight.ExtraBold),
        Font(R.font.sel, FontWeight.ExtraLight),
        Font(R.font.sl, FontWeight.Light),
        Font(R.font.sm, FontWeight.Medium),
        Font(R.font.sr, FontWeight.Normal),
        Font(R.font.ssm, FontWeight.SemiBold),
        Font(R.font.st, FontWeight.Thin),
    )

    val chakraPetch = FontFamily(
        Font(R.font.cpb, FontWeight.Bold),
        Font(R.font.cpb, FontWeight.ExtraBold),
        Font(R.font.cpl, FontWeight.ExtraLight),
        Font(R.font.cpl, FontWeight.Light),
        Font(R.font.cpm, FontWeight.Medium),
        Font(R.font.cpr, FontWeight.Normal),
        Font(R.font.cpsb, FontWeight.SemiBold),
        Font(R.font.cpl, FontWeight.Thin),
    )
}