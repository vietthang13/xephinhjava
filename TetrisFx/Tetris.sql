USE [Tetris]
GO

/****** Object:  Table [dbo].[Score]    Script Date: 17/8/2019 11:03:03 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Score](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NULL,
	[Score] [int] NULL
) ON [PRIMARY]

GO


